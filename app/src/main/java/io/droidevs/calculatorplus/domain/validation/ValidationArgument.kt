package io.droidevs.calculatorplus.domain.validation

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.token.LinkedToken

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