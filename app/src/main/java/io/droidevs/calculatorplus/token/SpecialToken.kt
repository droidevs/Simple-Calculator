package io.droidevs.calculatorplus.token

import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.components.Digit
import io.droidevs.calculatorplus.components.Special
import io.droidevs.calculatorplus.validation.ValidationArgument

class SpecialToken(special : Special) : LinkedToken(special) {

    override fun isValid(argument: ValidationArgument): Boolean {
        return validateSpecial(argument.prev,argument.current as Special)
    }

    private fun validateSpecial(prev: Component?, special: Special): Boolean {
        // Rule 2: If there is no preceding component, the decimal operator is invalid
        if (prev == null) {
            return false
        }

        return when (prev) {
            // Rule 1: A decimal operator can validly follow a digit
            is Digit -> {
                true
            }

            // Rule 3: Any other preceding component makes the decimal operator invalid
            else -> {
                false
            }
        }
    }

    class DecimalToken() : LinkedToken(Special.Decimal) {

        companion object {

            fun get() : DecimalToken {
                return DecimalToken()
            }
        }
    }

    class EmptyToken() : LinkedToken(Special.Empty) {
        companion object {
            fun get() : EmptyToken {
                return EmptyToken()
            }
        }
    }

}