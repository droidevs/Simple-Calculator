package io.droidevs.calculatorplus.components

abstract class Constant(text : String) : io.droidevs.calculatorplus.components.Component(text = text) {

    abstract fun apply() : Double

    object PI : Constant("π") {
        override fun apply(): Double {
            return 3.14159265358979323846
        }
    }

    object E : Constant("e") {
        override fun apply(): Double {
            return 2.71828182845904523536
        }
    }
}