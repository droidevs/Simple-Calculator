package io.droidevs.calculatorplus.ui.action


open class DigitAction(text: String) : Action(text) {

    object ZeroAction : DigitAction("0")

    object OneAction : DigitAction("1")

    object TwoAction : DigitAction("2")

    object ThreeAction : DigitAction("3")

    object FourAction : DigitAction("4")

    object FiveAction : DigitAction("5")

    object SixAction : DigitAction("6")

    object SevenAction : DigitAction("7")

    object EightAction : DigitAction("8")

    object NineAction : DigitAction("9")

}