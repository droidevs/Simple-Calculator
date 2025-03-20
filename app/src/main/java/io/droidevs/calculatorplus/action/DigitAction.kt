package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.components.Digit


open class DigitAction(text: String, value: ActionValue) : Action(text, value) {

    object ZeroAction : DigitAction("0", ActionValue.of(Digit.Zero))

    object OneAction : DigitAction("1", ActionValue.of(Digit.One))

    object TwoAction : DigitAction("2", ActionValue.of(Digit.Two))

    object ThreeAction : DigitAction("3", ActionValue.of(Digit.Three))

    object FourAction : DigitAction("4", ActionValue.of(Digit.Four))

    object FiveAction : DigitAction("5", ActionValue.of(Digit.Five))

    object SixAction : DigitAction("6", ActionValue.of(Digit.Six))

    object sevenAction : DigitAction("7", ActionValue.of(Digit.Seven))

    object EightAction : DigitAction("8", ActionValue.of(Digit.Eight))

    object NineAction : DigitAction("9", ActionValue.of(Digit.Nine))

}