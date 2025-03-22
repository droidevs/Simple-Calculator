package io.droidevs.calculatorplus.token

import io.droidevs.calculatorplus.components.ClcFunction
import io.droidevs.calculatorplus.components.Component
import io.droidevs.calculatorplus.components.Digit
import io.droidevs.calculatorplus.components.Operator
import io.droidevs.calculatorplus.components.Parenthesis
import io.droidevs.calculatorplus.validation.ValidationArgument

open class ParenthesisToken(parenthesis : Parenthesis) : LinkedToken(parenthesis) {


    override fun isValid(argument: ValidationArgument): Boolean {
        return validateParenthesis(argument.prev,argument.current as Parenthesis)
    }

    private fun validateParenthesis(prev: Component?, parenthesis: Parenthesis): Boolean {
        // Rule 1: If there is no preceding component
        if (prev == null) {
            return if (parenthesis == Parenthesis.OpenParenthesis) {
                true // Open parenthesis can be the first input
            } else {
                false // Close parenthesis cannot be the first input
            }
        }

        return when (prev) {
            // Rule 2: If the preceding component is a digit
            is Digit -> {
                if (parenthesis == Parenthesis.CloseParenthesis) {
                    true // Close parenthesis is valid after a digit
                } else {
                    false // Open parenthesis is invalid after a digit
                }
            }

            // Rule 3: If the preceding component is a function
            is ClcFunction -> {
                if (parenthesis == Parenthesis.OpenParenthesis) {
                    true // Open parenthesis is valid after a function
                } else {
                    false // Close parenthesis is invalid after a function
                }
            }

            // Rule 3 (continued): If the preceding component is an operator
            is Operator -> {
                if (parenthesis == Parenthesis.OpenParenthesis) {
                    true // Open parenthesis is valid after an operator
                } else {
                    false // Close parenthesis is invalid after an operator
                }
            }

            // Rule 4: Any other component makes the parenthesis invalid
            else -> {
                false
            }
        }
    }

    class OpenParenthesisToken() : ParenthesisToken(Parenthesis.OpenParenthesis) {

        companion object {
            fun get() : OpenParenthesisToken {
                return OpenParenthesisToken()
            }
        }

    }

    class CloseParenthesisToken() : ParenthesisToken(Parenthesis.CloseParenthesis){

        companion object {
            fun get() : CloseParenthesisToken {
                return CloseParenthesisToken()
            }
        }

    }

    fun isOpenParenthesis() : Boolean {
        return this is OpenParenthesisToken
    }

    fun isCloseParenthesis() : Boolean {
        return this is CloseParenthesisToken
    }

}