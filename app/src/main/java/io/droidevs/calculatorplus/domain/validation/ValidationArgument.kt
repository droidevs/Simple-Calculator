package io.droidevs.calculatorplus.domain.validation

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.LinkedToken

class ValidationArgument(
    val prev: Component = Special.Empty,
    val current: Component,
    val next: Component = Special.Empty
) {

    companion object {
        fun of(token: LinkedToken) : ValidationArgument {
            val prev = token.prev?.component ?: Special.Empty
            val next = token.next?.component ?: Special.Empty
            return ValidationArgument(prev, token.component, next)
        }
    }
}