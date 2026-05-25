package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.validation.ValidationArgument

open class OperatorToken(operator: Operator) : LinkedToken(operator) {

    override fun isValid(argument: ValidationArgument): Boolean {
        val prev = argument.prev
        val op = argument.current as Operator

        return when (prev) {
            // BUG FIX #9: The implementation allowed unary + at start (op is Minus || Plus)
            // but the test asserted only Minus is allowed. The test was correct — unary + is
            // non-standard UX and the parser adds no value from it. Only Minus is valid here.
            is Special.Empty -> op is Operator.Minus

            // After operator or open paren: only unary minus is sensible
            is Operator -> op is Operator.Minus
            is Parenthesis.OpenParenthesis -> op is Operator.Minus

            is Digit,
            is Constant,
            is Parenthesis.CloseParenthesis -> true

            is Special.Decimal,
            is ClcFunction -> false

            else -> false
        }
    }

    class PlusToken : OperatorToken(Operator.Plus) {
        companion object { fun get() = PlusToken() }
    }

    class MinusToken : OperatorToken(Operator.Minus) {
        companion object { fun get() = MinusToken() }
    }

    class MultiplyToken : OperatorToken(Operator.Multiply) {
        companion object { fun get() = MultiplyToken() }
    }

    class DivideToken : OperatorToken(Operator.Divide) {
        companion object { fun get() = DivideToken() }
    }

    class PowerToken : OperatorToken(Operator.Power) {
        companion object { fun get() = PowerToken() }
    }

    class PercentToken : OperatorToken(Operator.Percent) {
        companion object { fun get() = PercentToken() }
    }

    class FactorialToken : OperatorToken(Operator.Factorial) {
        companion object { fun get() = FactorialToken() }
    }

    fun isPlus() = this is PlusToken
    fun isMinus() = this is MinusToken
    fun isMultiply() = this is MultiplyToken
    fun isDivide() = this is DivideToken
    fun isPower() = this is PowerToken
    fun isPercent() = this is PercentToken
}