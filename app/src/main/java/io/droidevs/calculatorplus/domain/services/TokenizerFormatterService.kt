package io.droidevs.calculatorplus.domain.services

import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.Constant
import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.ConstantToken
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.refreshIndicesFromThisAsHead

class TokenizerFormatterService {
    fun format(expression: String): LinkedToken {
        var exp = removeSeparators(expression)
        return tokenize(exp)
    }

    private fun removeSeparators(text: String): String {
        return text.replace(",", "")
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

            is Constant.PI, is Constant.E -> ConstantToken(component as Constant)

            is Operator.Plus -> OperatorToken.PlusToken.get()
            is Operator.Minus -> OperatorToken.MinusToken.get()
            is Operator.Multiply -> OperatorToken.MultiplyToken.get()
            is Operator.Divide -> OperatorToken.DivideToken.get()
            is Operator.Power -> OperatorToken.PowerToken.get()
            is Operator.Percent -> OperatorToken.PercentToken.get()

            is Parenthesis.OpenParenthesis -> ParenthesisToken.OpenParenthesisToken.get()
            is Parenthesis.CloseParenthesis -> ParenthesisToken.CloseParenthesisToken.get()

            is Special.Decimal -> SpecialToken.DecimalToken.get()

            // NOTE: functions are multi-character ("sin"), so we can't reconstruct them from a plain String here.
            // In this app the LinkedToken chain is the source of truth.
            else -> null
        }
    }

    /**
     * Tokenizes a mathematical expression string into tokens.
     * @param expression The formatted expression string.
     * @return A TokenProvider containing the head of the linked tokens.
     */
    private fun tokenize(expression: String): LinkedToken {
        var head: LinkedToken? = null
        var tail: LinkedToken? = null

        for (c in expression) {
            val component = Component.identify(c)
            val newToken = provide(component) ?: continue

            if (head == null) {
                head = newToken
                tail = newToken
            } else {
                tail!!.next = newToken
                newToken.prev = tail
                tail = newToken
            }
        }

        val result = head ?: SpecialToken.EmptyToken.get().apply { startIndex = 0 }

        // Add empty sentinels at both ends so validation rules can treat them as Special.Empty.
        if (head != null && tail != null) {
            val emptyHead = SpecialToken.EmptyToken()
            emptyHead.startIndex = 0
            result.prev = emptyHead
            emptyHead.next = result

            val emptyTail = SpecialToken.EmptyToken()
            tail.next = emptyTail
            emptyTail.prev = tail
        }

        result.refreshIndicesFromThisAsHead()
        return result
    }

    /**
     * Convert cursor position from formatted string (number separators ex 3,222,333) to raw string (no number separators).
     */
    fun cursorFormattedToRaw(formatted: String, raw: String, formattedCursor: Int): Int {
        var rawIndex = 0
        var fIndex = 0

        while (fIndex < formattedCursor && rawIndex < raw.length) {
            if (formatted[fIndex] == raw[rawIndex]) {
                rawIndex++
            }
            fIndex++
        }
        return rawIndex
    }


}