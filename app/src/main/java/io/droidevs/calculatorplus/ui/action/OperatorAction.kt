package io.droidevs.calculatorplus.ui.action


abstract class OperatorAction(text: String) : Action(text) {

    object Plus : OperatorAction("+")
    object Minus : OperatorAction("-")
    object Multiply : OperatorAction("*")

    object Divide : OperatorAction("/")

    object Percent : OperatorAction("%")

    object Factorial : OperatorAction("x!")

    object Power : OperatorAction("^")

}
