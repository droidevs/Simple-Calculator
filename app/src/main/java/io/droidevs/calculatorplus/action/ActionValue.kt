package io.droidevs.calculatorplus.action

import io.droidevs.calculatorplus.components.Component


class ActionValue(val components : List<Component> = emptyList()) {

    companion object {
        fun of(component: Component) : ActionValue {
            return ActionValue(listOf(component))
        }

        fun of(vararg components: Component) : ActionValue {
            return ActionValue(components.toList())
        }
    }

    fun getValue() : String{

        var value = ""
        for (component in components) {
            value += component.text
        }
        return value
    }
}