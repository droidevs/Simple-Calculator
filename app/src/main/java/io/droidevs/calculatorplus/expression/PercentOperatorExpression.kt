package io.droidevs.calculatorplus.expression

import io.droidevs.calculatorplus.components.Operator
import java.math.BigDecimal


class PercentOperatorExpression(
    val expression: Expression
): OperatorExpression(Operator.Percent) {

    override fun evaluate(): BigDecimal {
        return expression.evaluate().divide(BigDecimal(100.0))
    }
}