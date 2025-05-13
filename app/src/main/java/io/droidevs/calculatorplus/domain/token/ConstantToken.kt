package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.validation.ValidationArgument


class ConstantToken(constant: Constant) : LinkedToken(constant) {

    override fun isValid(argument: ValidationArgument): Boolean {
        return validateConstant(argument.prev,argument.current as Constant)
    }

    private fun validateConstant(prev: Component?, constant: Constant): Boolean {
        if (prev == null) {
            return true // Constants are valid when there is no preceding component.
        }

        return when (prev) {
            is Operator -> {
                validateConstant(prev, constant) // Delegate validation based on the specific operator rules.
            }
            is Parenthesis -> {
                validateConstant(prev, constant) // Delegate validation based on the specific parenthesis rules.
            }
            else -> {
                false // Any other preceding component invalidates the constant.
            }
        }
    }

    private fun validateConstant(prev: Parenthesis, constant: Constant): Boolean {
        return when (prev) {
            is Parenthesis.OpenParenthesis -> {
                true // Constants are valid immediately after an open parenthesis.
            }
            else -> {
                false // Constants are invalid after a close parenthesis or unsupported parenthesis types.
            }
        }
    }

    private fun validateConstant(prev: Operator, constant: Constant): Boolean {
        return when (prev) {
            is Operator.Percent -> {
                false
            }
            else -> {
                true
            }
        }
    }


}