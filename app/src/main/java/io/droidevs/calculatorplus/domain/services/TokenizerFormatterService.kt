package io.droidevs.calculatorplus.domain.services

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken

class TokenizerFormatterService {
    fun format(expression: String): LinkedToken {
        var exp = removeSeparators(expression)
        return tokenize(exp)
    }

    private fun removeSeparators(text: String): String {
        return text.replace(Special.Decimal.text, "")
    }

    private fun provide(component: Component) : LinkedToken? {
        return when(component) {
            is Digit.Zero -> DigitToken.ZeroToken.get()
            is Digit.One -> DigitToken.OneToken.get()
            is Digit.Two -> DigitToken.TwoToken.get()
            is Digit.Three -> DigitToken.ThreeToken.get()
            is Digit.Four -> DigitToken.FourToken.get()
            is Digit.Five -> DigitToken.FiveToken.get()
            is Digit.Six -> DigitToken.SixToken.get()
            is Digit.Seven -> DigitToken.SevenToken.get()
            is Digit.Eight -> DigitToken.EightToken.get()
            is Digit.Nine -> DigitToken.NineToken.get()
            is ClcFunction.Cos -> FunctionToken.CosToken.get()
            is ClcFunction.Sin -> FunctionToken.SinToken.get()
            is ClcFunction.CosH -> FunctionToken.CosHToken.get()
            is ClcFunction.SinH -> FunctionToken.SinHToken.get()
            is ClcFunction.TanH -> FunctionToken.TanHToken.get()
            is ClcFunction.ACos -> FunctionToken.ACosToken.get()
            is ClcFunction.ASin -> FunctionToken.ASinToken.get()
            is ClcFunction.ATan -> FunctionToken.ATanToken.get()
            is ClcFunction.ACosH -> FunctionToken.ACosHToken.get()
            is ClcFunction.ASinH -> FunctionToken.ASinHToken.get()
            is ClcFunction.ATanH -> FunctionToken.ATanHToken.get()
            is ClcFunction.Tan -> FunctionToken.TanToken.get()
            is ClcFunction.Log -> FunctionToken.LogToken.get()
            is Operator.Plus -> OperatorToken.PlusToken.get()
            is Operator.Minus -> OperatorToken.MinusToken.get()
            is Operator.Multiply -> OperatorToken.MultiplyToken.get()
            is Operator.Divide -> OperatorToken.DivideToken.get()
            is Operator.Power -> OperatorToken.PowerToken.get()
            is Operator.Percent -> OperatorToken.PercentToken.get()
            is Parenthesis.OpenParenthesis -> ParenthesisToken.OpenParenthesisToken.get()
            is Parenthesis.CloseParenthesis -> ParenthesisToken.CloseParenthesisToken.get()
            is Special.Decimal -> SpecialToken.DecimalToken.get()
            else -> null
        }
    }

    /**
     * Tokenizes a mathematical expression string into tokens.
     * @param expression The formatted expression string.
     * @return A TokenProvider containing the head of the linked tokens.
     */
    private fun tokenize(expression: String): LinkedToken {
        //todo : fix it
        var head: LinkedToken = SpecialToken.EmptyToken()
        var token: LinkedToken = SpecialToken.EmptyToken()

        for (c in expression) {
            val component = Component.identify(c)

            // Handle the current non-number component.
            val newToken = provide(component)
            if (newToken != null) {
                if (head == SpecialToken.EmptyToken()) {
                    head = newToken
                    token = head
                } else {
                    token.next = newToken
                    newToken.prev = token
                    token = newToken
                }
            }
        }

        return head
    }
}