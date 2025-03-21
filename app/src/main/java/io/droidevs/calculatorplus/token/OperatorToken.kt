package io.droidevs.calculatorplus.token

import io.droidevs.calculatorplus.components.ClcFunction
import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.components.Digit
import io.droidevs.calculatorplus.components.Operator
import io.droidevs.calculatorplus.components.Parenthesis
import io.droidevs.calculatorplus.components.Special
import io.droidevs.calculatorplus.validation.ValidationArgument


/**
 * Represents a token for mathematical operators.
 *
 * This class and its subclasses encapsulate individual operators (e.g., `+`, `-`, `*`, `/`, etc.)
 * in a tokenized expression. Each operator token is linked within a `LinkedToken` structure.
 *
 * @param operator The operator component associated with this token.
 */
open class OperatorToken(operator: Operator) : LinkedToken(operator) {


    override fun isValid(argument: ValidationArgument): Boolean {
        return super.isValid(argument)
    }

    fun validateOperator(prev: Component?, operator: Operator): Boolean {
        // Rule: If the operator is at the start of the expression
        if (prev == null) {
            return when (operator) {
                // Rule: Minus operator is allowed at the start of the expression
                is Operator.Minus -> {
                    true
                }
                // Rule: Other operators are not allowed at the start
                else -> {
                    false
                }
            }
        }

        // Rule: Validate based on the type of preceding component
        return when (prev) {
            // Rule 1: An operator can directly follow a digit
            is Digit -> {
                true
            }
            // Rule: An operator cannot follow a decimal point
            is Special.Decimal -> {
                false
            }
            // Rule: Two operators cannot appear consecutively
            is Operator -> {
                return  validateOperator(prev,operator)
            }
            // Rule: Operators cannot directly follow functions (e.g., "sin+")
            is ClcFunction -> {
                false
            }
            // Rule: Defer validation for operators following parentheses
            is Parenthesis -> {
                validateOperator(prev, operator)
            }
            // Rule: Any other cases result in invalid placement
            else -> {
                false
            }
        }
    }

    private fun validateOperator(prev: Operator, operator: Operator): Boolean {
        return when (prev) {
            // Rule: If the previous operator is '%'
            is Operator.Percent -> {
                // Rule: Consecutive '%' operators are invalid
                if (operator is Operator.Percent) {
                    false
                } else {
                    // Any other operator after '%' is valid
                    true
                }
            }
            is Operator.Power -> {
                if (operator is Operator.Plus || operator is Operator.Minus)
                    true
                else
                    false
            }
            // All other cases are invalid
            else -> false
        }
    }

    private fun validateOperator(prev: Parenthesis, operator: Operator): Boolean {
        return when (prev) {
            // Rule: An operator is valid after a closing parenthesis
            is Parenthesis.CloseParenthesis -> {
                true
            }
            else -> {
                when (operator) {
                    // Rule: A minus operator is valid in other cases
                    is Operator.Minus -> {
                        true
                    }
                    // All other operators are invalid in this context
                    else -> {
                        false
                    }
                }
            }
        }
    }

    /**
     * Token representing the `+` operator.
     */
    class PlusToken : OperatorToken(Operator.Plus) {

        companion object {
            /**
             * Creates and returns a new instance of `PlusToken`.
             *
             * @return A new `PlusToken` instance.
             */
            fun get(): PlusToken {
                return PlusToken()
            }
        }
    }

    /**
     * Token representing the `-` operator.
     */
    class MinusToken : OperatorToken(Operator.Minus) {

        companion object {
            /**
             * Creates and returns a new instance of `MinusToken`.
             *
             * @return A new `MinusToken` instance.
             */
            fun get(): MinusToken {
                return MinusToken()
            }
        }
    }

    /**
     * Token representing the `*` operator (multiplication).
     */
    class MultiplyToken : OperatorToken(Operator.Multiply) {

        companion object {
            /**
             * Creates and returns a new instance of `MultiplyToken`.
             *
             * @return A new `MultiplyToken` instance.
             */
            fun get(): MultiplyToken {
                return MultiplyToken()
            }
        }
    }

    /**
     * Token representing the `/` operator (division).
     */
    class DivideToken : OperatorToken(Operator.Divide) {

        companion object {
            /**
             * Creates and returns a new instance of `DivideToken`.
             *
             * @return A new `DivideToken` instance.
             */
            fun get(): DivideToken {
                return DivideToken()
            }
        }
    }

    /**
     * Token representing the `^` operator (exponentiation).
     */
    class PowerToken : OperatorToken(Operator.Power) {

        companion object {
            /**
             * Creates and returns a new instance of `PowerToken`.
             *
             * @return A new `PowerToken` instance.
             */
            fun get(): PowerToken {
                return PowerToken()
            }
        }
    }

    /**
     * Token representing the `%` operator (percentage).
     */
    class PercentToken : OperatorToken(Operator.Percent) {

        companion object {
            /**
             * Creates and returns a new instance of `PercentToken`.
             *
             * @return A new `PercentToken` instance.
             */
            fun get(): PercentToken {
                return PercentToken()
            }
        }
    }

    /**
     * Checks if this token is a `+` operator.
     *
     * @return `true` if this token is a `PlusToken`; `false` otherwise.
     */
    fun isPlus(): Boolean {
        return this is PlusToken
    }

    /**
     * Checks if this token is a `-` operator.
     *
     * @return `true` if this token is a `MinusToken`; `false` otherwise.
     */
    fun isMinus(): Boolean {
        return this is MinusToken
    }

    /**
     * Checks if this token is a `*` operator.
     *
     * @return `true` if this token is a `MultiplyToken`; `false` otherwise.
     */
    fun isMultiply(): Boolean {
        return this is MultiplyToken
    }

    /**
     * Checks if this token is a `/` operator.
     *
     * @return `true` if this token is a `DivideToken`; `false` otherwise.
     */
    fun isDivide(): Boolean {
        return this is DivideToken
    }

    /**
     * Checks if this token is a `^` operator.
     *
     * @return `true` if this token is a `PowerToken`; `false` otherwise.
     */
    fun isPower(): Boolean {
        return this is PowerToken
    }

    /**
     * Checks if this token is a `%` operator.
     *
     * @return `true` if this token is a `PercentToken`; `false` otherwise.
     */
    fun isPercent(): Boolean {
        return this is PercentToken
    }
}
