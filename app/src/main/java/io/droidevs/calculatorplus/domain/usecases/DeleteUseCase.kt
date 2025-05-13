package io.droidevs.calculatorplus.domain.usecases

import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.model.Calculation
import io.droidevs.calculatorplus.domain.services.EvaluatorService
import io.droidevs.calculatorplus.domain.services.ExpressionDisplayFormatter
import io.droidevs.calculatorplus.domain.services.TokenizerFormatterService
import io.droidevs.calculatorplus.domain.token.FunctionToken
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.ParenthesisToken
import io.droidevs.calculatorplus.domain.token.isFunction
import io.droidevs.calculatorplus.domain.token.isNotEmpty
import io.droidevs.calculatorplus.domain.token.isOpenParenthesis

class DeleteUseCase(
    private val tokenizerFormatter: TokenizerFormatterService,
    private val displayFormatter: ExpressionDisplayFormatter,
    private val evaluator: EvaluatorService
) {
    fun invoke(calculation: Calculation, pos: Int): Calculation {
        val prepared = tokenizerFormatter.format(calculation.expression)
        val newTokens = doDelete(prepared, pos)
        val result = evaluator.evaluate(newTokens)
        return Calculation(
            expression = displayFormatter.format(newTokens).toString(),
            result = result,
        )
    }

    private fun doDelete(token: LinkedToken, pos: Int): LinkedToken {
        var current : LinkedToken = token
        while (current.isNotEmpty()){
            if (pos == current.startIndex){
                val prev = current.prev
                if (prev.isOpenParenthesis()){
                    val prevToOpenParenthesis = prev.prev
                    if (prevToOpenParenthesis.isFunction()) {
                        val p = prevToOpenParenthesis.prev
                        current.prev = p
                        p.next = current
                        return token
                    }
                }
            }
            if (pos in current.startIndex+1..current.endIndex){
                if (current is FunctionToken){
                    val prev = current.prev
                    val next =
                        if(current.next.isOpenParenthesis()){
                        current.next.next
                        }else {
                            current.next
                        }
                    next.prev = prev
                    prev.next = next
                    return token
                }
                val prev = current.prev
                val next = current.next
                next.prev = prev
                prev.next = next
            }
            current = current.next
        }

        return token
    }
}