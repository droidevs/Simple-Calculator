package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Operator
import io.droidevs.calculatorplus.domain.components.isMinus
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.result.InvalidOperatorInPositionError
import io.droidevs.calculatorplus.domain.result.InvalidPositionError
import io.droidevs.calculatorplus.domain.result.Result
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.find
import io.droidevs.calculatorplus.domain.token.getTokenAt
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
    fun invoke(calculation: Calculation,operator: Operator, pos: Int): Calculation {
        val formatted = tokenizerFormatter.format(calculation.expression)
        val result = doInput(formatted,operator, pos)
        result.fold(
            onSuccess = { result ->
                val bigDecimalResult = evaluator.evaluate(result)
                return Calculation(
                    expression = displayFormatter.format(result).toString(),
                    result = bigDecimalResult
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
    ): Result<LinkedToken> {
        // Case 0: empty expression
        if (expression.isEmpty()) {
            return if (operator.isMinus()) {
                Result.Success(operator.toToken())
            } else {
                Result.Success(expression)
            }
        }

        // Find the token at this position
        val current = expression.getTokenAt(pos)

        // Determine previous token safely
        val prev = current?.prev ?: run {
            // If inserting at the end, use the last token as prev
            var last: LinkedToken = expression
            while (last.next.isNotEmpty()) last = last.next
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
            return Result.Success(expression.replaceAt(prev.startIndex, operator.toToken()))
        }

        // Case 4: current token is operator → replace it
        if (current?.isOperator() == true) {
            return Result.Success(expression.replaceAt(pos, operator.toToken()))
        }

        // Case 5: default → insert operator at this position
        return Result.Success(expression.insertAt(pos, operator.toToken()))
    }

} 