package io.droidevs.calculatorplus.domain.services

import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.TokenProvider
import java.math.BigDecimal

class EvaluatorService {

    fun evaluate(tokens: LinkedToken) : BigDecimal{
        var exp = TokenProvider.construct(tokens).parseExpression()
        return exp.evaluate()
    }

}