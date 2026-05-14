package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


class ConstantToken(constant: Constant) : LinkedToken(constant) {

    override fun isValid(argument: ValidationArgument): Boolean {
        val prev = argument.prev
        return when (prev) {
            is io.droidevs.calculatorplus.domain.components.Special.Empty -> true
            is Operator -> prev !is Operator.Percent
            is Parenthesis.OpenParenthesis -> true
            else -> false
        }
    }


}