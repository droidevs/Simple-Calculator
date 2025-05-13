package io.droidevs.calculatorplus.ui.action

open class ConstantAction(text: String) : Action(text) {

    object PI : ConstantAction("π")
    object E : ConstantAction("e")
}