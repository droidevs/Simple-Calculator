package io.droidevs.calculatorplus.expression

import java.math.BigDecimal


sealed class Expression() {

    abstract fun evaluate(): BigDecimal

}