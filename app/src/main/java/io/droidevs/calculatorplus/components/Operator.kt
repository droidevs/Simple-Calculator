package io.droidevs.calculatorplus.components

abstract class Operator(text: String) : Component(text= text) {

    companion object {
        fun getAll(): List<Operator> {
            return listOf(Plus, Minus, Multiply, Divide, Percent)
        }

        fun isOperator(input: String): Boolean {
            return getAll().any { it.text == input }
        }
    }

    object Plus : Operator("+")

    object Minus : Operator("-")

    object Multiply : Operator("*")

    object Divide : Operator("/")

    object Percent : Operator("%")

    object Factorial : Operator("!")

    object Power : Operator("^")

}
