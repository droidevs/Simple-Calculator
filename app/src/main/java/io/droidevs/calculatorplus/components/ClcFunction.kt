package io.droidevs.calculatorplus.components

sealed class ClcFunction(text: String) : Component(text = text) {
    companion object {
        fun getAllFunctionOperators(): List<ClcFunction> {
            return listOf(
                Sin, Cos, Tan, Ln, Log, SquareRoot, Absolute
            )
        }

        fun isFunction(input: String): Boolean {
            return getAllFunctionOperators().any { it.text == input }
        }
    }

    // Function definitions
    object Sin : ClcFunction("sin")

    object Cos : ClcFunction("cos")

    object Tan : ClcFunction("tan")


    object Ln : ClcFunction("ln")

    object Log : ClcFunction("log")


    object SquareRoot : ClcFunction("√")

    object Absolute : ClcFunction("abs")

    // Additional missing operators
    object ASin : ClcFunction("asin")

    object ACos : ClcFunction("acos")

    object ATan : ClcFunction("atan")

    object SinH : ClcFunction("sinh")

    object CosH : ClcFunction("cosh")

    object TanH : ClcFunction("tanh")

    object ASinH : ClcFunction("asinh")

    object ACosH : ClcFunction("acosh")

    object ATanH : ClcFunction("atanh")

}

