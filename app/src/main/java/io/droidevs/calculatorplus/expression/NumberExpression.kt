package io.droidevs.calculatorplus.expression

import io.droidevs.calculatorplus.expression.Expression
import java.math.BigDecimal

class NumberExpression(private val value: Double) : Expression() {
    override fun evaluate(): BigDecimal = BigDecimal(value)
}