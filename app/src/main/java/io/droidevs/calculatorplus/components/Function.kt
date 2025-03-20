package io.droidevs.calculatorplus.components

sealed class Function(text: String) : io.droidevs.calculatorplus.components.Component(text = text) {
    companion object {
        fun getAllFunctionOperators(): List<Function> {
            return listOf(
                Sin, Cos, Tan, Ln, Log, SquareRoot, Absolute
            )
        }

        fun isFunction(input: String): Boolean {
            return getAllFunctionOperators().any { it.text == input }
        }
    }

    // Function definitions
    object Sin : Function("sin")

    object Cos : Function("cos")

    object Tan : Function("tan")


    object Ln : Function("ln")

    object Log : Function("log")


    object SquareRoot : Function("√")

    object Absolute : Function("abs")

    // Additional missing operators
    object ASin : Function("asin")

    object ACos : Function("acos")

    object ATan : Function("atan")

    object SinH : Function("sinh")

    object CosH : Function("cosh")

    object TanH : Function("tanh")

    object ASinH : Function("asinh")

    object ACosH : Function("acosh")

    object ATanH : Function("atanh")

}

