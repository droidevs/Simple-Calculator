package io.droidevs.calculatorplus.expression

import java.math.BigDecimal


data object NoneExp : Expression() {

    override fun evaluate(): BigDecimal {
        throw UnsupportedOperationException("Not implemented")
    }

}