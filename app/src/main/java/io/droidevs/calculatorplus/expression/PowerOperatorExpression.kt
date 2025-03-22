package io.droidevs.calculatorplus.expression

import io.droidevs.calculatorplus.components.Operator
import java.math.BigDecimal
import java.math.MathContext

class PowerOperatorExpression(
    val baseExpression: Expression,
    val exponentExpression: Expression
): OperatorExpression(Operator.Power) {
    override fun evaluate(): BigDecimal {
        return baseExpression.evaluate().pow(exponentExpression.evaluate().toInt(), MathContext.DECIMAL64)
    }

}