package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Digit
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.getTokenAt
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDecimal
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isEToken

class DigitUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    companion object {
        private const val MAX_NUMBER_LENGTH = 15
    }

    operator fun invoke(calculation: Calculation, digit: Digit): Calculation {
        val currentTokens = calculation.tokens.headToken()

        // Cursor mapping must be done using the *current* raw vs formatted expression.
        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        val (newRawPos, newTokens) = doInput(currentTokens, digit, rawPos)

        val expPair = displayFormatter.format(newTokens)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()

        val eval = evaluator.evaluate(newTokens)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, newRawPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, newRawPos),
                    error = error
                )
            }
        )
    }

    private fun doInput(expression: LinkedToken, digit: Digit, pos: Int): Pair<Int, LinkedToken> {
        val head = expression.headToken()

        // Don't allow insertion in the middle of an existing token.
        if (head.find { pos in (it.startIndex + 1)..it.endIndex } != null) {
            return pos to head
        }

        // Determine the token immediately before the cursor.
        val prev = head.find { it.endIndex == pos - 1 } ?: head.prev ?: SpecialToken.EmptyToken()

        if (prev.isEToken()) {
            return pos to head
        }

        var adjustedPos = pos
        var result: LinkedToken = head

        // Implicit multiply: ")" + digit => ")×digit"
        if (prev.isCloseParenthesis()) {
            result = result.insertAt(adjustedPos, OperatorToken.MultiplyToken())
            adjustedPos += 1
        }

        // Enforce max digits within the number we are editing.
        if (countDigitsInNumberAround(result, adjustedPos) >= MAX_NUMBER_LENGTH) {
            return pos to head
        }

        result = result.insertAt(adjustedPos, digit.toToken())
        adjustedPos += 1

        return adjustedPos to result.headToken()
    }

    private fun countDigitsInNumberAround(expression: LinkedToken, pos: Int): Int {
        val head = expression.headToken()
        val left = head.find { it.endIndex == pos - 1 }
        val right = head.getTokenAt(pos)

        val pivot = when {
            left != null && (left.isDigit() || left.isDecimal()) -> left
            right != null && (right.isDigit() || right.isDecimal()) -> right
            else -> null
        } ?: return 0

        var start = pivot
        while (start.prev?.let { it.isDigit() || it.isDecimal() } == true) {
            start = start.prev!!
        }

        var count = 0
        var cur: LinkedToken? = start
        while (cur != null && (cur.isDigit() || cur.isDecimal())) {
            if (cur.isDigit()) count++
            cur = cur.next
        }

        return count
    }
}