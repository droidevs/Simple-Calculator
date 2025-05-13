package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDecimal
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isEToken
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isNotEmpty

class DigitUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    companion object {
        private const val MAX_NUMBER_LENGTH = 15
    }

    operator fun invoke(calculation: Calculation, digit: Digit, pos: Int): Calculation {
        val formatted = tokenizerFormatter.format(calculation.expression)
        val result = doInput(expression = formatted,digit = digit, pos = pos)
        val bigDecimalResult = evaluator.evaluate(result)
        return Calculation(
            expression = displayFormatter.format(result).toString(),
            result = bigDecimalResult,
        )
    }

    private fun doInput(expression: LinkedToken,digit: Digit, pos: Int): LinkedToken {
        if (expression.isEmpty()) {
            return digit.toToken()
        }

        var current : LinkedToken = expression
        while(current.isNotEmpty()) {
            if (pos == current.startIndex) {
                val prev = current.prev

                if (current.isDigit() || current.isDecimal() || current.isEmpty()){
                    if (prev.isDigit() || prev.isDecimal() || prev.isEmpty()){
                        if (getCurrentNumberLength(current) >= MAX_NUMBER_LENGTH)
                            return expression
                        else {
                            val digitToken = digit.toToken()
                            digitToken.prev = prev
                            prev.next = digitToken
                            current.prev = digitToken
                            digitToken.next = current
                            return expression
                        }
                    }
                }

                if (prev.isCloseParenthesis()){
                    val multiplyToken = OperatorToken.MultiplyToken()
                    multiplyToken.prev = prev
                    prev.next = multiplyToken
                    val digitToken = digit.toToken()
                    digitToken.prev = multiplyToken
                    multiplyToken.next = digitToken
                    current.prev = digitToken
                    digitToken.next = current
                    return expression
                }

                if (prev.isEToken()){
                    return expression
                }
                val digitToken = digit.toToken()
                digitToken.prev = prev
                prev.next = digitToken
                current.prev = digitToken
                digitToken.next = current
                return expression
            }
            if (pos in current.startIndex+1..current.endIndex)
                return expression
        }
        val digitToken = digit.toToken()
        digitToken.prev = current
        current.next = digitToken
        return expression
    }

    private fun getCurrentNumberLength(token: LinkedToken): Int {
        var length = 0
        var current = if(token.isDigit()) token else token.prev
        while (current.prev.isDigit()) {
            length++
            current = current.prev!!
        }
        current = token.next
        while (current.isDigit()) {
            length++
            current = current.next
        }
        
        return length
    }
} 