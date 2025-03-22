package io.droidevs.calculatorplus.expression

import java.math.BigDecimal


class EmptyExpression : Expression() {
    override fun evaluate(): BigDecimal {
        throw IllegalStateException("Empty expression cannot be evaluated")
    }
}