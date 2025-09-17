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
import io.droidevs.calculatorplus.domain.token.getTokenAt
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
        val formatted = tokenizerFormatter.format(calculation.expression)
        val result = doInput(formatted, pos)
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
        pos: Int
    ): Result<LinkedToken> {
        // Count open/close parentheses in the chain
        val openCount = expression.count { it.isOpenParenthesis() }
        val closeCount = expression.count { it.isCloseParenthesis() }

        // Get the token before `pos`
        val prevToken = expression.getTokenAt(pos - 1)

        // If we're at the start or after an operator/open parenthesis → insert opening parenthesis
        if (pos == 0 || prevToken?.let { it.isOperator() || it.isOpenParenthesis() } == true) {
            return Result.Success(expression.insertAt(pos, ParenthesisToken.OpenParenthesisToken()))
        }

        // If we're after a digit or closing parenthesis → insert closing parenthesis (only if we have unmatched opens)
        if (prevToken?.let { it.isDigit() || it.isCloseParenthesis() } == true) {
            if (openCount > closeCount) {
                return Result.Success(expression.insertAt(pos, ParenthesisToken.CloseParenthesisToken()))
            }
        }

        // Case 3: default → insert "*" + "("
        val mulToken = OperatorToken.MultiplyToken()
        expression.insertAt(pos, mulToken)
        expression.insertAt(pos + 1, ParenthesisToken.OpenParenthesisToken())

        return Result.Success(expression)
    }

} 