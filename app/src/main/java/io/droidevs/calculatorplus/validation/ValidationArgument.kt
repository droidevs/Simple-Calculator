package io.droidevs.calculatorplus.validation

import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.token.LinkedToken

class ValidationArgument(
    val prev: Component?,
    val current: Component,
    val next: Component?
) {

    companion object {
        fun of(token: LinkedToken) : ValidationArgument {
            return ValidationArgument(token.prev?.component,token.component,token.next?.component)
        }
    }
}