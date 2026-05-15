package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.DigitToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDecimal
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isEToken
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import io.droidevs.calculatorplus.domain.token.isOperator

class DecimalUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluatorService: EvaluatorService
) {
    fun invoke(calculation: Calculation, pos: Int): Calculation {
        val currentTokens = calculation.tokens.headToken()
        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, pos)

        val (adjustedPos, newTokens) = doInput(currentTokens, rawPos)
        val expPair = displayFormatter.format(newTokens)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()

        val eval = evaluatorService.evaluate(newTokens)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    error = error
                )
            }
        )
    }

    private fun doInput(expression: LinkedToken, pos: Int): Pair<Int, LinkedToken> {
        // Case 1: Empty expression
        if (expression.isEmpty()) {
            val zero = DigitToken.ZeroToken()
            val decimal = SpecialToken.DecimalToken()
            zero.next = decimal
            decimal.prev = zero
            return 2 to zero
        }

        val head = expression.headToken()

        // Don't allow insertion in the middle of an existing token.
        if (head.find { pos in (it.startIndex + 1)..it.endIndex } != null) {
            return pos to head
        }

        // Determine the token immediately before the cursor.
        val prev = head.find { it.endIndex == pos - 1 } ?: head.prev ?: SpecialToken.EmptyToken()

        // If we're inside a number that already has a decimal (or we're in the exponent), do nothing.
        if ((prev.isDigit() || prev.isDecimal()) && isInNumberWithDecimal(prev)) {
            return pos to head
        }

        var adjustedPos = pos
        var out: LinkedToken = head

        // Case 2: after close parenthesis => implicit multiply then "0."
        if (prev.isCloseParenthesis()) {
            out = out.insertAt(adjustedPos, OperatorToken.MultiplyToken())
            adjustedPos += 1
            out = out.insertAt(adjustedPos, DigitToken.ZeroToken())
            adjustedPos += 1
            out = out.insertAt(adjustedPos, SpecialToken.DecimalToken())
            adjustedPos += 1
            return adjustedPos to out.headToken()
        }

        // Case 3: after operator/open paren/empty/E => insert "0."
        if (prev.isOperator() || prev.isOpenParenthesis() || prev.isEmpty() || prev.isEToken()) {
            out = out.insertAt(adjustedPos, DigitToken.ZeroToken())
            adjustedPos += 1
            out = out.insertAt(adjustedPos, SpecialToken.DecimalToken())
            adjustedPos += 1
            return adjustedPos to out.headToken()
        }

        // Case 4: after digit => insert just '.'
        if (prev.isDigit()) {
            out = out.insertAt(adjustedPos, SpecialToken.DecimalToken())
            adjustedPos += 1
            return adjustedPos to out.headToken()
        }

        return pos to head
    }

    private fun isAfterOperatorOrOpenParenthesis(token: LinkedToken): Boolean {
        val prev = token.prev
        if (prev == null || prev.isEmpty()) return true
        return prev.isOperator() || prev.isOpenParenthesis()
    }

    private fun isInNumberWithDecimal(token: LinkedToken): Boolean {
        var current: LinkedToken?

        // Check backward for existing decimal or exponent.
        current = token
        while (current != null) {
            when {
                current.isDecimal() -> return true
                current.isEToken() -> return true // can't add decimal in exponent
                current.isDigit() -> { /* Continue */ }
                else -> break
            }
            current = current.prev
        }

        // Check forward for existing decimal or exponent.
        current = token
        while (current != null) {
            when {
                current.isDecimal() -> return true
                current.isEToken() -> return false // exponent ahead; decimal can exist before it
                current.isDigit() -> { /* Continue */ }
                else -> break
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
