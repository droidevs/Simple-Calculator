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
import io.droidevs.calculatorplus.domain.token.isEmpty
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis
import io.droidevs.calculatorplus.domain.token.isOperator

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

    private fun doInput(expression: LinkedToken,operator: Operator, pos: Int): Result<LinkedToken> {
        if (expression.isEmpty()){
            if (operator.isMinus()){
                return Result.Success(operator.toToken())
            }
            else {
                return Result.Success(expression)
            }
        }

        var current = expression
        while(current.isNotEmpty()) {
            if (pos == current.startIndex){
                val prev = current.prev
                val next = current.next
                if (prev.isOperator()){
                    val prevToOperator = prev.prev
                    val tokenOperator = operator.toToken()
                    tokenOperator.prev = prevToOperator
                    prevToOperator.next = tokenOperator
                    current.prev = tokenOperator
                    tokenOperator.next = current
                    return Result.Success(expression)
                }
                if (current.isOperator()){
                    val tokenOperator = operator.toToken()
                    tokenOperator.prev = prev
                    prev.next = tokenOperator
                    next.prev = tokenOperator
                    tokenOperator.next = next
                    return Result.Success(expression)
                }
                if (prev.isOpenParenthesis()){
                    if (operator.isMinus()){
                        val tokenOperator = operator.toToken()
                        tokenOperator.prev = prev
                        prev.next = tokenOperator
                        current.prev = tokenOperator
                        tokenOperator.next = current
                        return Result.Success(expression)
                    }
                    else {
                        return Result.Error(InvalidOperatorInPositionError())
                    }
                }
                val operatorToken = operator.toToken()
                operatorToken.prev = prev
                prev.next = operatorToken
                current.prev = operatorToken
                operatorToken.next = current
                return Result.Success(expression)
            }
            if (pos in current.startIndex+1..current.endIndex){
                return Result.Error(InvalidPositionError())
            }
            current = current.next
        }

        val lastNotNull = current.prev
        val operatorToken = operator.toToken()
        operatorToken.prev = lastNotNull
        lastNotNull.next = operatorToken
        return Result.Success(expression)
    }
} 