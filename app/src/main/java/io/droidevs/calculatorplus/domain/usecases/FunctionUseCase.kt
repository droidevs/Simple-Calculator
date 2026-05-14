package io.droidevs.calculatorplus.domain.usecases

import androidx.compose.ui.graphics.vector.EmptyPath
import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.ConstantToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.getTokenAt
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isFunction
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import kotlin.math.exp
import kotlin.math.max

class FunctionUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    companion object {
        private const val MAX_NESTED_FUNCTIONS = 5
    }

    fun invoke(calculation: Calculation,function: ClcFunction): Calculation{
        val currentTokens = calculation.tokens.headToken()

        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        val (adjustedPos, resultLinkedToken) = doInput(currentTokens, function, rawPos)

        val expPair = displayFormatter.format(resultLinkedToken)
        val rawExp = expPair.first.toString()
        val formattedExp = expPair.second.toString()

        val eval = evaluator.evaluate(resultLinkedToken)
        return eval.fold(
            onSuccess = { value ->
                Calculation(
                    tokens = resultLinkedToken.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    result = value,
                    error = null
                )
            },
            onFailure = { error ->
                calculation.copy(
                    tokens = resultLinkedToken.headToken(),
                    expression = formattedExp,
                    pos = displayFormatter.cursorRawToFormatted(rawExp, formattedExp, adjustedPos),
                    error = error
                )
            }
        )
    }

    private fun doInput(
        expression: LinkedToken,
        function: ClcFunction,
        pos: Int
    ): Pair<Int, LinkedToken> {
        val head = expression.headToken()

        // Check maximum nested functions (insertion adds +1 level at the cursor).
        if (willExceedNestingIfInsertFunction(head, pos, MAX_NESTED_FUNCTIONS)) {
            return pos to head
        }

        // Don't allow insertion in the middle of an existing token.
        if (head.find { pos in (it.startIndex + 1)..it.endIndex } != null) {
            return pos to head
        }

        val prev = head.find { it.endIndex == pos - 1 } ?: head.prev ?: SpecialToken.EmptyToken()

        var adjustedPos = pos
        var out: LinkedToken = head

        // Implicit multiply when function follows a value.
        if (prev.isDigit() || prev.isCloseParenthesis() || prev is ConstantToken) {
            out = out.insertAt(adjustedPos, OperatorToken.MultiplyToken())
            adjustedPos += 1
        }

        out = out.insertAt(adjustedPos, function.toToken())
        adjustedPos += function.text.length

        out = out.insertAt(adjustedPos, ParenthesisToken.OpenParenthesisToken())
        adjustedPos += 1

        return adjustedPos to out.headToken()
    }

    private fun willExceedNestingIfInsertFunction(startToken: LinkedToken, pos: Int, maxNested: Int): Boolean {
        var depth = 0
        var current: LinkedToken? = startToken.headToken()

        // Count only function-parenthesis nesting: "fn(" increments, ")" decrements.
        while (current != null && current.isNotEmpty() && current.endIndex < pos) {
            if (current.isOpenParenthesis() && current.prev?.isFunction() == true) {
                depth++
            } else if (current.isCloseParenthesis() && depth > 0) {
                depth--
            }
            current = current.next
        }

        // Inserting a new function adds one nesting level at the cursor.
        return (depth + 1) > maxNested
    }

}
