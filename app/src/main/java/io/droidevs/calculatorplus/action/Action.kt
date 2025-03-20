package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.action.ActionValue


abstract class Action(var text: String, var value : ActionValue){


    fun provide() : String = value.getValue()

}