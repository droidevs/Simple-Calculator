package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.isFunction
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis

class DeleteUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    fun invoke(calculation: Calculation, pos: Int): Calculation {
        val currentTokens = calculation.tokens.headToken()
        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, pos)

        val newTokens = doDelete(currentTokens, rawPos)
        val expPair = displayFormatter.format(newTokens)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()
        val adjustedRawPos = (rawPos - 1).coerceAtLeast(0).coerceAtMost(rawExp.length)

        val eval = evaluator.evaluate(newTokens)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedRawPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = newTokens.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedRawPos),
                    error = error
                )
            }
        )
    }

    private fun doDelete(token: LinkedToken, pos: Int): LinkedToken {
        var current: LinkedToken? = token
        while (current != null && current.isNotEmpty()) {
            val cur = current

            // Backspace at the start of a token: remove what sits immediately before the cursor.
            if (pos == cur.startIndex) {
                val prev = cur.prev
                if (prev?.isOpenParenthesis() == true) {
                    val prevToOpenParenthesis = prev.prev
                    if (prevToOpenParenthesis?.isFunction() == true) {
                        val p = prevToOpenParenthesis.prev
                        cur.prev = p
                        p?.next = cur
                        return cur.headToken()
                    }
                }
            }

            // Cursor inside a multi-char token: remove the whole token.
            if (pos in (cur.startIndex + 1)..cur.endIndex) {
                if (cur is FunctionToken) {
                    val prev = cur.prev
                    val next = if (cur.next?.isOpenParenthesis() == true) cur.next?.next else cur.next

                    next?.prev = prev
                    prev?.next = next

                    return (next ?: prev ?: SpecialToken.EmptyToken()).headToken()
                }

                val prev = cur.prev
                val next = cur.next

                next?.prev = prev
                prev?.next = next

                return (next ?: prev ?: SpecialToken.EmptyToken()).headToken()
            }

            current = cur.next
        }

        return token.headToken()
    }
}
