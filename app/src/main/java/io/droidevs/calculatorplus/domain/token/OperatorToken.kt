package io.droidevs.calculatorplus.domain.token

import androidx.compose.runtime.currentComposer
import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


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
        val prev = argument.prev
        val op = argument.current as Operator

        return when (prev) {
            is Special.Empty -> op is Operator.Minus || op is Operator.Plus

            // unary + / - is allowed after an operator or an open parenthesis
            is Operator -> op is Operator.Minus || op is Operator.Plus
            is Parenthesis.OpenParenthesis -> op is Operator.Minus || op is Operator.Plus

            is Digit,
            is Constant,
            is Parenthesis.CloseParenthesis -> true

            is Special.Decimal,
            is ClcFunction -> false

            else -> false
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

    class FactorialToken : OperatorToken(Operator.Factorial) {
        companion object {
            /**
             * Creates and returns a new instance of `FactorialToken`.
             *
             * @return A new `FactorialToken` instance.
             */
            fun get(): FactorialToken {
                return FactorialToken()
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
