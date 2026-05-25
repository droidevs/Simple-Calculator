package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.components.Operator
import java.math.BigDecimal


class ArithmeticOperatorExpression(
    operator: Operator,
    val leftExpression: Expression,
    val rightExpression: Expression
) : OperatorExpression(operator) {

    override fun evaluate(): BigDecimal {
        return when (operator) {
            is Operator.Plus -> leftExpression.evaluate().plus(rightExpression.evaluate())
            is Operator.Minus -> leftExpression.evaluate().minus(rightExpression.evaluate())
            is Operator.Multiply -> leftExpression.evaluate().multiply(rightExpression.evaluate())
            is Operator.Divide -> {
                // BUG FIX #2: BigDecimal.divide() throws ArithmeticException when divisor is
                // exactly zero, even with a MathContext. Guard explicitly so EvaluatorService
                // can surface a user-friendly DivisionByZeroError instead of a crash.
                val divisor = rightExpression.evaluate()
                if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                    throw ArithmeticException("Division by zero")
                }
                leftExpression.evaluate().divide(divisor, java.math.MathContext.DECIMAL64)
            }
            else -> throw IllegalArgumentException("Invalid arithmetic operator: $operator")
        }
    }
}