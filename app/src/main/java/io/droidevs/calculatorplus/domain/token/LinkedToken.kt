package io.droidevs.calculatorplus.domain.token

import androidx.annotation.CallSuper
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import io.droidevs.calculatorplus.domain.validation.ValidationResult

/**
 * Abstract class representing a linked token used in expression tokenization.
 *
 * Tokens are created by converting string expressions into components, and each token
 * represents an individual part of the expression, such as a digit, operator, or function.
 * The tokens are linked together in a doubly linked structure to improve performance during
 * validation and processing operations.
 *
 * @param component The component associated with this token.
 */
open class LinkedToken(var component: Component) {

    /**
     * The length of this token, calculated as the length of the text representation
     * of the associated component.
     */
    val length: Int
        get() {
            return component.text.length
        }

    /**
     * The starting index of this token in the expression.
     * This value is dynamically updated based on its position relative to the previous token.
     */
    var startIndex: Int = -1

    /**
     * The ending index of this token in the expression.
     * Computed as the starting index plus the length of the token.
     */
    val endIndex: Int
        get() = startIndex + length - 1

    /**
     * A reference to the next token in the linked structure.
     */
    var next: LinkedToken = SpecialToken.EmptyToken()

    private fun refreshIndex(){
        startIndex = prev?.let { it.endIndex + 1 }?: 0
        next?.refreshIndex()
    }
    /**
     * A reference to the previous token in the linked structure.
     * Updating this value also recalculates the `startIndex` of the current token.
     */
    var prev: LinkedToken = SpecialToken.EmptyToken()
        set(value) {
            field = value
            if (value != null)
                startIndex = value.endIndex + 1
            else
                startIndex = 0 // If there's no previous token, start at index 0
            refreshIndex()
        }

    /**
     * Validates that the starting index of this token is consistent with its position
     * in the linked structure.
     *
     * @return `true` if the starting index is valid; `false` otherwise.
     */
    private fun validateIndex(): Boolean {
        return if (prev == null) {
            startIndex == 0
        } else {
            startIndex == prev!!.endIndex + 1
        }
    }

    /**
     * Validates this token and its linked successors using a `ValidatorService`.
     *
     * Validation ensures that:
     * 1. The index alignment within the linked structure is correct.
     * 2. The token adheres to rules defined by the `ValidatorService`.
     * 3. All subsequent tokens in the chain are also valid.
     *
     * @param vs The `ValidatorService` instance used for validation.
     * @return A `ValidationResult` indicating whether the token chain is valid.
     */
    @CallSuper
    final fun validate(): ValidationResult {
        if (!validateIndex()) {
            return ValidationResult.Invalid // Index misalignment
        }
        if (!isValid(ValidationArgument.of(this))) {
            return ValidationResult.Invalid // Validation service check failed
        }
        next?.let {
            return it.validate() // Recursively validate the next token
        }
        return ValidationResult.Valid // All tokens are valid
    }

    open fun isValid(argument: ValidationArgument) : Boolean {
        return true
    }

}

fun LinkedToken.isEmpty() : Boolean  = this is SpecialToken.EmptyToken

fun LinkedToken.isNotEmpty() : Boolean  = !isEmpty()

/**
 * Checks if this token represents an operator.
 *
 * @return `true` if the token is an `OperatorToken`; `false` otherwise.
 */
fun LinkedToken.isOperator(): Boolean = this is OperatorToken

/**
 * Checks if this token represents a decimal point.
 *
 * @return `true` if the token is a `DecimalToken`; `false` otherwise.
 */
fun LinkedToken.isDecimal(): Boolean = this is SpecialToken.DecimalToken

/**
 * Checks if this token represents a digit.
 *
 * @return `true` if the token is a `DigitToken`; `false` otherwise.
 */
fun LinkedToken.isDigit(): Boolean = this is DigitToken

/**
 * Checks if this token represents a mathematical function.
 *
 * @return `true` if the token is a `FunctionToken`; `false` otherwise.
 */
fun LinkedToken.isFunction(): Boolean = this is FunctionToken

/**
 * Checks if this token represents a parenthesis.
 *
 * @return `true` if the token is a `ParenthesisToken`; `false` otherwise.
 */
fun LinkedToken.isParenthesis(): Boolean = this is ParenthesisToken

fun LinkedToken.isOpenParenthesis(): Boolean = this is ParenthesisToken.OpenParenthesisToken

fun LinkedToken.isCloseParenthesis(): Boolean = this is ParenthesisToken.CloseParenthesisToken

fun LinkedToken.isSpecial(): Boolean = this is SpecialToken

fun LinkedToken.isEToken() : Boolean = this is SpecialToken.EToken

/**
 * Counts the number of tokens in the chain that match the given [predicate].
 *
 * @param predicate A function that returns true for tokens to be counted.
 * @return The number of matching tokens in the chain.
 */
fun LinkedToken.count(predicate: (LinkedToken) -> Boolean): Int {
    var current: LinkedToken? = this
    var count = 0
    while (current != null && current.isNotEmpty()) {
        if (predicate(current)) {
            count++
        }
        current = current.next
    }
    return count
}

/**
 * Find the token that starts at the given `startIndex`.
 */
fun LinkedToken.getTokenAt(startIndex: Int): LinkedToken? {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex == startIndex) return current
        current = current.next
    }
    return null
}

/**
 * Insert a new token at a specific absolute `startIndex`.
 */
fun LinkedToken.insertAt(startIndex: Int, newToken: LinkedToken): LinkedToken {
    // If inserting before the very first token
    if (startIndex <= this.startIndex) {
        newToken.next = this
        this.prev = newToken
        return newToken
    }

    // Walk until we find the token that starts after our desired index
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex >= startIndex) {
            val prevToken = current.prev
            prevToken.next = newToken
            newToken.prev = prevToken
            newToken.next = current
            current.prev = newToken
            return this
        }
        current = current.next
    }

    // If we reach the end → append
    var tail: LinkedToken = this
    while (tail.next.isNotEmpty()) {
        tail = tail.next
    }
    tail.next = newToken
    newToken.prev = tail
    return this
}

/**
 * Replace the token that starts at [pos] with [newToken].
 * If no token starts exactly at [pos], nothing happens.
 */
fun LinkedToken.replaceAt(pos: Int, newToken: LinkedToken): LinkedToken {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex == pos) {
            current.replaceWith(newToken)
            // return head
            return if (this.prev == null) this else {
                var head = this
                while (head.prev != null) head = head.prev!!
                head
            }
        }
        current = current.next
    }
    return this
}

/**
 * Replace this token with [newToken].
 */
fun LinkedToken.replaceWith(newToken: LinkedToken) {
    val prevToken = this.prev
    val nextToken = this.next

    newToken.prev = prevToken
    newToken.next = nextToken

    prevToken?.next = newToken
    nextToken?.prev = newToken
}


/**
 * Find the first token in the linked list that matches the [predicate].
 */
inline fun LinkedToken.find(predicate: (LinkedToken) -> Boolean): LinkedToken? {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (predicate(current)) return current
        current = current.next
    }
    return null
}

