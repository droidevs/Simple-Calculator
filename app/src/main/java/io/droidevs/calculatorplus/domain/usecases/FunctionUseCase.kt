package io.droidevs.calculatorplus.domain.usecases

import androidx.compose.ui.graphics.vector.EmptyPath
import io.droidevs.calculatorplus.domain.components.ClcFunction
import io.droidevs.calculatorplus.domain.components.toToken
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.OperatorToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
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

    fun invoke(calculation: Calculation,function: ClcFunction, pos: Int): Calculation {
        val formatted = tokenizerFormatter.format(calculation.expression)
        val result = doInput(formatted,function, pos)
        val bigDecimalResult = evaluator.evaluate(result)
        return Calculation(
            expression = displayFormatter.format(result).toString(),
            result = bigDecimalResult,
        )
    }

    private fun doInput(expression: LinkedToken,function: ClcFunction, pos: Int): LinkedToken {
        // Check for maximum nested functions
        if (!willExceedNestingIfInsertFunction(expression, pos, MAX_NESTED_FUNCTIONS))
            return expression
        var current = expression
        while (current.isNotEmpty()) {
            if (pos == current.startIndex) {
                val token = current
                val prev = token.prev
                val next = token.next

                // If we're at the start, just add the function with parentheses
                if (token.isEmpty()) {
                    val functionToken = function.toToken()
                    val openParenthesis = ParenthesisToken.OpenParenthesisToken()
                    functionToken.prev = prev
                    prev.next = functionToken
                    openParenthesis.prev = functionToken
                    functionToken.next = openParenthesis
                    return functionToken
                }
                if (prev.isDigit() || prev.isCloseParenthesis()){
                    val multiptyToken = OperatorToken.MultiplyToken()
                    multiptyToken.prev = prev
                    prev.next = multiptyToken
                    val functionToken = function.toToken()
                    val openParenthesis = ParenthesisToken.OpenParenthesisToken()
                    functionToken.prev = multiptyToken
                    multiptyToken.next = functionToken
                    openParenthesis.prev = functionToken
                    functionToken.next = openParenthesis
                    token.prev = openParenthesis
                    openParenthesis.next = token
                    return expression
                }
                val functionToken = function.toToken()
                val openParenthesis = ParenthesisToken.OpenParenthesisToken()
                functionToken.prev = prev
                prev.next = functionToken
                openParenthesis.prev = functionToken
                functionToken.next = openParenthesis
                token.prev = openParenthesis
                openParenthesis.next = token
                return expression
            }
            if (pos in current.startIndex +1 .. current.endIndex) return expression
            current = if (pos < current.startIndex) current.prev else current.next
        }
        val functionToken = function.toToken()
        if (current.isNotEmpty()){
            functionToken.prev = current
            current.next = functionToken
        }
        val openParenthesis = ParenthesisToken.OpenParenthesisToken()
        openParenthesis.prev = functionToken
        functionToken.next = openParenthesis
        return if (current.isEmpty()) functionToken else expression
    }

    private fun willExceedNestingIfInsertFunction(startToken: LinkedToken, pos: Int, maxNested: Int): Boolean {
        var top = 0
        var temp = 0
        var count = 0
        var current : LinkedToken = startToken
        while (current.isNotEmpty() && current.startIndex <= pos) {
            if (current.isOpenParenthesis() && current.prev.isFunction()){
                count++
                temp++
            }
            if (current.isCloseParenthesis()){
                count--
                if (count == 0){
                    top = max(top, temp)
                    temp = 0
                }
            }
            current = current.next
        }
        count++
        temp++

        while (current.isNotEmpty()) {
            if (current.isOpenParenthesis() && current.prev.isFunction()){
                count++
                temp++
            }
            if (current.isCloseParenthesis()){
                count--
                if (count == 0){
                    top = max(top, temp)
                    temp = 0
                }
            }
            current = current.next
        }
        return top < maxNested
    }

}

fun findTokenAtPositionLinked(start: LinkedToken, position: Int): LinkedToken {
    var current = start
    while (current.isNotEmpty()) {
        if (position == current.startIndex) return current
        if (position in current.startIndex +1 .. current.endIndex) return start
        current = if (position < current.startIndex) current.prev else current.next
    }
    return current
}