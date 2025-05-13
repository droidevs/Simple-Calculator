package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isFunction
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOperator

class DecimalUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluatorService: EvaluatorService
) {
    fun invoke(calculation: Calculation, pos: Int): Calculation {
        val prepared = tokenizerFormatter.format(calculation.expression)
        val newTokens = doInput(prepared, pos)
        val result = evaluatorService.evaluate(newTokens)
        return Calculation(
            expression = displayFormatter.format(newTokens).toString(),
            result = result,
        )
    }

    private fun doInput(expression: LinkedToken?, pos: Int): LinkedToken {
        // Case 1: Empty expression or at start
        if (expression == null){
            val zero = DigitToken.ZeroToken()
            val decimal = SpecialToken.DecimalToken()
            zero.next = decimal
            decimal.prev = zero
            return zero
        }

        var current : LinkedToken = expression
        while(current.isNotEmpty()){
            if(pos == current.startIndex){

                val prev = current.prev
                // Case 2: After an operator or opening parenthesis
                if (prev.isOperator() || prev == ParenthesisToken.OpenParenthesisToken()) {
                    val zero = DigitToken.ZeroToken()
                    zero.prev = prev
                    prev.next = zero
                    val decimal = SpecialToken.DecimalToken()
                    zero.next = decimal
                    decimal.prev = zero
                    decimal.next = current
                    current.prev = decimal
                    return expression
                }
                // Case 3: After a closing parenthesis
                if (prev == ParenthesisToken.CloseParenthesisToken()) {
                    val multiplication = OperatorToken.MultiplyToken()
                    multiplication.prev = prev
                    prev.next = multiplication
                    val zero = DigitToken.ZeroToken()
                    multiplication.next = zero
                    zero.prev = multiplication
                    val decimal = SpecialToken.DecimalToken()
                    zero.next = decimal
                    decimal.prev = zero
                    decimal.next = current
                    current.prev = decimal
                    return expression
                }
                // Case 4: Check if we're in a number and if it already has a decimal point
                if (isInNumberWithDecimal(prev)){
                    return expression
                }
                // Case 5: After a number, just add decimal point
                if (prev.isDigit()) {
                    val decimal = SpecialToken.DecimalToken()
                    decimal.prev = prev
                    decimal.next = current
                    current.prev = decimal
                    return expression
                }
                if (prev == SpecialToken.EToken()){
                    val zero = DigitToken.ZeroToken()
                    val decimal = SpecialToken.DecimalToken()
                    zero.next = decimal
                    decimal.prev = zero
                    return zero
                }
            }
            if(pos in current.startIndex+1 .. current.endIndex){
                return expression
            }
            current = current.next
        }
        return expression
    }

    private fun isAfterOperatorOrOpenParenthesis(token: LinkedToken): Boolean {
        val prev = token.prev
        if (prev == null || prev == SpecialToken.EmptyToken()) return true
        return prev.isOperator() || prev == ParenthesisToken.OpenParenthesisToken()
    }

    private fun isInNumberWithDecimal(token: LinkedToken): Boolean {
        var current: LinkedToken?

        // Check backward for existing decimal or exponent
        current = token
        while (current != null) {
            when {
                current == SpecialToken.DecimalToken() -> {
                    return true
                }
                current == SpecialToken.EToken() -> {
                    // Can't have decimal after exponent
                    return true
                }
                current.isDigit() -> { /* Continue */ }
                else -> break // Exit at non-number character
            }
            current = current.prev
        }

        // Check forward for existing decimal or exponent
        current = token
        while (current != null) {
            when {
                current == SpecialToken.DecimalToken() -> {
                    // Found decimal ahead - invalid
                    return true
                }
                current == SpecialToken.EToken() -> {
                    // If exponent ahead, decimal can only exist before exponent
                    return false
                }
                current.isDigit() -> { /* Continue */ }
                else -> break // Exit at non-number character
            }
            current = current.next
        }

        return false
    }

    private fun isInScientificNotation(expression: String, pos: Int): Boolean {
        var i = pos - 1
        while (i >= 0) {
            when {
                expression[i] == 'e' || expression[i] == 'E' -> return true
                expression[i].isDigit() || expression[i] == Special.Decimal.text[0] -> {
                    // Continue checking
                }
                else -> break
            }
            i--
        }
        return false
    }
} 