package io.droidevs.calculatorplus.components

sealed class Special(text: String,value: String = text) : Component(text = text){

    object Empty : Special("")
    object Unknown : Special("?")
    object Decimal : Special(".")

}
