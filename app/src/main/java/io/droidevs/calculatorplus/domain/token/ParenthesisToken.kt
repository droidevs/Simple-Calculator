package io.droidevs.calculatorplus.domain.token

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.validation.ValidationArgument

open class ParenthesisToken(parenthesis : Parenthesis) : LinkedToken(parenthesis) {


    override fun isValid(argument: ValidationArgument): Boolean {
        val prev = argument.prev
        val par = argument.current as Parenthesis

        return when (par) {
            is Parenthesis.OpenParenthesis -> {
                prev is Special.Empty || prev is Operator || prev is Parenthesis.OpenParenthesis || prev is ClcFunction
            }

            is Parenthesis.CloseParenthesis -> {
                prev is Digit || prev is io.droidevs.calculatorplus.domain.components.Constant || prev is Parenthesis.CloseParenthesis
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