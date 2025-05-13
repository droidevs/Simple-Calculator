package io.droidevs.calculatorplus.domain.expression

import io.droidevs.calculatorplus.domain.expression.Expression
import java.math.BigDecimal

class NumberExpression(private val value: Double) : Expression() {
    override fun evaluate(): BigDecimal = BigDecimal(value)
}