package io.droidevs.calculatorplus.domain.expression

import java.math.BigDecimal


sealed class Expression() {

    abstract fun evaluate(): BigDecimal

}