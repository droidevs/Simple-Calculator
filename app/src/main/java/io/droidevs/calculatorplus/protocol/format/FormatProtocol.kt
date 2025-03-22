package io.droidevs.calculatorplus.protocol.format

import io.droidevs.calculatorplus.components.Component


class FormatProtocol(var component : Component, var replace : String = component.text) {


    var proSize = component.text.length

    fun format(expression: String) : String {
        return expression.replace(component.text, replace)
    }

    fun effective() : Boolean {
        return component.text != replace
    }
}
