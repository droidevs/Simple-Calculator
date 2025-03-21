package io.droidevs.calculatorplus.ui.action



abstract class FunctionAction(text: String) : Action(text) {


    object Sin : FunctionAction("sin")

    object Cos : FunctionAction("cos")

    object Tan : FunctionAction("tan")

    object Ln : FunctionAction("ln")

    object Log : FunctionAction("log")

    object Square : FunctionAction("x²")

    object SquareRoot : FunctionAction("√")

    object Absolute : FunctionAction("|x|")

    object Power : FunctionAction("xʸ")

    // Additional missing operators
    object ASin : FunctionAction("sin⁻¹")

    object ACos : FunctionAction("cos⁻¹")

    object ATan : FunctionAction("tan⁻¹")

    object SinH : FunctionAction("sinh")
    object CosH : FunctionAction("cosh")
    object TanH : FunctionAction("tanh")
    object ASinH : FunctionAction("sinh⁻¹")

    object ACosH : FunctionAction("cosh⁻¹")

    object ATanH : FunctionAction("tanh⁻¹")

    object Cube : FunctionAction("x³")

    object PowerE : FunctionAction("exp")

}