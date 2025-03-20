package io.droidevs.calculatorplus.action


abstract class ApplyAction(text: String, value: ActionValue) : Action(text, value) {

    abstract fun apply(argument: Argument) : Double

}