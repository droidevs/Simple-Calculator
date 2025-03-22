package io.droidevs.calculatorplus.token

import androidx.annotation.CallSuper
import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.validation.ValidationArgument
import io.droidevs.calculatorplus.validation.ValidationResult

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
        get() = startIndex + length

    /**
     * A reference to the next token in the linked structure.
     */
    var next: LinkedToken? = null

    /**
     * A reference to the previous token in the linked structure.
     * Updating this value also recalculates the `startIndex` of the current token.
     */
    var prev: LinkedToken? = null
        set(value) {
            field = value
            if (value != null)
                startIndex = value.endIndex + 1
            else
                startIndex = 0 // If there's no previous token, start at index 0
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

    /**
     * Checks if this token represents an operator.
     *
     * @return `true` if the token is an `OperatorToken`; `false` otherwise.
     */
    fun isOperator(): Boolean = this is OperatorToken

    /**
     * Checks if this token represents a decimal point.
     *
     * @return `true` if the token is a `DecimalToken`; `false` otherwise.
     */
    fun isDecimal(): Boolean = this is SpecialToken.DecimalToken

    /**
     * Checks if this token represents a digit.
     *
     * @return `true` if the token is a `DigitToken`; `false` otherwise.
     */
    fun isDigit(): Boolean = this is DigitToken

    /**
     * Checks if this token represents a mathematical function.
     *
     * @return `true` if the token is a `FunctionToken`; `false` otherwise.
     */
    fun isFunction(): Boolean = this is FunctionToken

    /**
     * Checks if this token represents a parenthesis.
     *
     * @return `true` if the token is a `ParenthesisToken`; `false` otherwise.
     */
    fun isParenthesis(): Boolean = this is ParenthesisToken
}
