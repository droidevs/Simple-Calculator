package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.Parenthesis
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.Result
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.count
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isCloseParenthesis
import io.droidevs.calculatorplus.domain.token.isDigit
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import io.droidevs.calculatorplus.domain.token.isOperator

class ParenthesesUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {

    fun invoke(calculation: Calculation,pos: Int): Calculation {
        val currentTokens = calculation.tokens.headToken()

        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        val result = doInput(currentTokens, rawPos)
        result.fold(
            onSuccess = { resultPair ->
                val adjustedPos = resultPair.first
                val resultLinkedToken = resultPair.second

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
            },
            onFailure = { error ->
                return calculation.copy(error = error)
            }
        )
    }

    private fun doInput(
        expression: LinkedToken,
        pos: Int
    ): Result<Pair<Int,LinkedToken>> {
        // Count open/close parentheses in the chain
        val openCount = expression.count { it.isOpenParenthesis() }
        val closeCount = expression.count { it.isCloseParenthesis() }

        val head = expression.headToken()

        // Get the token immediately before `pos`
        val prevToken = head.find { it.endIndex == pos - 1 }

        // If we're at the start or after an operator/open parenthesis → insert opening parenthesis
        if (pos == 0 || prevToken?.let { it.isOperator() || it.isOpenParenthesis() } != false) {
            val out = head.insertAt(pos, ParenthesisToken.OpenParenthesisToken())
            return Result.Success(pos + 1 to out)
        }

        // If we're after a digit or closing parenthesis → insert closing parenthesis (only if we have unmatched opens)
        if (prevToken != null && (prevToken.isDigit() || prevToken.isCloseParenthesis())) {
            if (openCount > closeCount) {
                val out = head.insertAt(pos, ParenthesisToken.CloseParenthesisToken())
                return Result.Success(pos + 1 to out)
            }
        }

        // Default → insert "*" + "("
        var out = head.insertAt(pos, OperatorToken.MultiplyToken())
        out = out.insertAt(pos + 1, ParenthesisToken.OpenParenthesisToken())

        return Result.Success(pos + 2 to out)
    }

} 