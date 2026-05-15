package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.isMinus
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.Result
import io.droidevs.calculatorplus.domain.result.errors.InvalidOperatorInPositionError
import io.droidevs.calculatorplus.domain.result.errors.InvalidPositionError
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.getTokenAt
import io.droidevs.calculatorplus.domain.token.headToken
import io.droidevs.calculatorplus.domain.token.insertAt
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import io.droidevs.calculatorplus.domain.token.isOperator
import io.droidevs.calculatorplus.domain.token.replaceAt

class OperationUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    fun invoke(calculation: Calculation,operator: Operator): Calculation {
        val currentTokens = calculation.tokens.headToken()

        val currentPair = displayFormatter.format(currentTokens)
        val rawNow = currentPair.first.toString()
        val formattedNow = currentPair.second.toString()
        val rawPos = tokenizerFormatter.cursorFormattedToRaw(formattedNow, rawNow, calculation.pos)

        val result = doInput(currentTokens, operator, rawPos)
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
        operator: Operator,
        pos: Int
    ): Result<Pair<Int, LinkedToken>> {
        // Case 0: empty expression
        if (expression.isEmpty()) {
            return if (operator.isMinus()) {
                Result.Success(pos + operator.text.length to operator.toToken())
            } else {
                Result.Success(pos to expression)
            }
        }

        // Find the token at this position
        val current = expression.getTokenAt(pos)

        // Determine previous token safely
        val prev = current?.prev ?: run {
            // If inserting at the end, use the last token as prev
            var last: LinkedToken = expression
            while (last.next?.isNotEmpty() == true) last = last.next!!
            last
        }

        // Case 1: position inside a token → invalid
        val midToken = expression.find { pos in it.startIndex + 1..it.endIndex }
        if (midToken != null) {
            return Result.Error(InvalidPositionError())
        }

        // Case 2: previous token is "(" → only unary minus allowed
        if (prev.isOpenParenthesis() && !operator.isMinus()) {
            return Result.Error(InvalidOperatorInPositionError())
        }

        // Case 3: previous token is operator → replace it
        if (prev.isOperator()) {
            return Result.Success(pos to expression.replaceAt(prev.startIndex, operator.toToken()))
        }

        // Case 4: current token is operator → replace it
        if (current?.isOperator() == true) {
            return Result.Success(pos to expression.replaceAt(pos, operator.toToken()))
        }

        // Case 5: default → insert operator at this position
        return Result.Success(pos + operator.text.length to expression.insertAt(pos, operator.toToken()))
    }

} 