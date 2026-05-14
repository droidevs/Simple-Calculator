package io.droidevs.calculatorplus.domain.token

import androidx.annotation.CallSuper
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.isE
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
        get() = component.text.length

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
    var next: LinkedToken? = null

    private fun refreshIndexForward() {
        var current: LinkedToken = this
        while (current.next?.isNotEmpty() == true) {
            val n = current.next!!
            n.startIndex = current.endIndex + 1
            current = n
        }
        // Keep a trailing empty token (if present) aligned as well.
        current.next?.startIndex = current.endIndex + 1
    }

    /**
     * A reference to the previous token in the linked structure.
     * Updating this value also recalculates the `startIndex` of the current token.
     */
    var prev: LinkedToken? = null
        set(value) {
            field = value
            startIndex = if (value?.isNotEmpty() == true) value.endIndex + 1 else 0
            refreshIndexForward()
        }

    /**
     * Validates that the starting index of this token is consistent with its position
     * in the linked structure.
     */
    private fun validateIndex(): Boolean {
        val p = prev
        return if (p?.isNotEmpty() != true) {
            startIndex == 0
        } else {
            startIndex == p.endIndex + 1
        }
    }

    /**
     * Validates this token and its linked successors.
     */
    @CallSuper
    final fun validate(): ValidationResult {
        if (!validateIndex()) return ValidationResult.Invalid
        if (!isValid(ValidationArgument.of(this))) return ValidationResult.Invalid

        val n = next
        return if (n != null && n.isNotEmpty()) n.validate() else ValidationResult.Valid
    }

    open fun isValid(argument: ValidationArgument): Boolean = true

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

fun LinkedToken.isEToken(): Boolean = this is ConstantToken && (this.component as io.droidevs.calculatorplus.domain.components.Constant).isE()

fun LinkedToken.headToken(): LinkedToken {
    var h = this
    while (h.prev?.isNotEmpty() == true) h = h.prev!!
    return h
}

fun LinkedToken.refreshIndicesFromThisAsHead() {
    var current: LinkedToken = this
    current.startIndex = 0
    while (current.next?.isNotEmpty() == true) {
        val n = current.next!!
        n.startIndex = current.endIndex + 1
        current = n
    }
    current.next?.startIndex = current.endIndex + 1
}

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
        val oldPrev = this.prev
        newToken.next = this
        newToken.prev = oldPrev ?: SpecialToken.EmptyToken()
        this.prev = newToken
        oldPrev?.next = newToken
        return newToken
    }

    // Walk until we find the token that starts at/after our desired index
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex >= startIndex) {
            val prevToken = current.prev ?: SpecialToken.EmptyToken()
            prevToken.next = newToken
            newToken.next = current
            newToken.prev = prevToken
            current.prev = newToken
            return this
        }
        current = current.next
    }

    // If we reach the end → append after the last non-empty token
    var tail: LinkedToken = this
    while (tail.next?.isNotEmpty() == true) {
        tail = tail.next!!
    }
    val end = tail.next // may be an EmptyToken or null
    tail.next = newToken
    newToken.next = end
    newToken.prev = tail
    end?.prev = newToken
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
            if (newToken.isNotEmpty()) {
                current.replaceWith(newToken)
            } else {
                val prevToken = current.prev
                val nextToken = current.next

                if (prevToken == null && nextToken == null) {
                    return SpecialToken.EmptyToken()
                }

                if (prevToken != null) prevToken.next = nextToken
                if (nextToken != null) nextToken.prev = prevToken
            }

            // return head
            return this.headToken()
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

    if (newToken.isNotEmpty()) {
        newToken.next = nextToken
        newToken.prev = prevToken

        prevToken?.next = newToken
        nextToken?.prev = newToken
    }
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

