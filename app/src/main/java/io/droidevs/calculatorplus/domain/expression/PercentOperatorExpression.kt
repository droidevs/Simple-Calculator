package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.components.Operator
import java.math.BigDecimal


class PercentOperatorExpression(
    val expression: Expression
): OperatorExpression(Operator.Percent) {

    override fun evaluate(): BigDecimal {
        return expression.evaluate().divide(BigDecimal("100"), java.math.MathContext.DECIMAL64)
    }
}