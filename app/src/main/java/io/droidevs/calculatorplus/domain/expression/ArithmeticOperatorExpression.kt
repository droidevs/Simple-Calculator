package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.components.Operator
import java.math.BigDecimal


class ArithmeticOperatorExpression(
    operator: Operator,
    val leftExpression: Expression,
    val rightExpression: Expression
) : OperatorExpression(operator) {

    override fun evaluate(): BigDecimal {
        when(operator){
            is Operator.Plus -> return leftExpression.evaluate().plus(rightExpression.evaluate())
            is Operator.Minus -> return leftExpression.evaluate().minus(rightExpression.evaluate())
            is Operator.Multiply -> return leftExpression.evaluate().multiply(rightExpression.evaluate())
            is Operator.Divide -> return leftExpression.evaluate().divide(rightExpression.evaluate(), java.math.MathContext.DECIMAL64)
            else -> {
                throw IllegalArgumentException("Invalid arithmetic operator: $operator")
            }
        }
    }

}