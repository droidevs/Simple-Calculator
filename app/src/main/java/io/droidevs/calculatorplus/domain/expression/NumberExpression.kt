package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.expression.Expression
import java.math.BigDecimal

class NumberExpression(private val value: BigDecimal) : Expression() {
    override fun evaluate(): BigDecimal = value
}