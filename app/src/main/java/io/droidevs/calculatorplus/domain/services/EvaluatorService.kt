package io.droidevs.calculatorplus.domain.services

import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.TokenProvider
import java.math.BigDecimal

class EvaluatorService {

    fun evaluate(tokens: LinkedToken) : io.droidevs.calculatorplus.domain.result.Result<BigDecimal> {
        return try {
            TokenProvider.construct(tokens).parse().fold(
                onSuccess = { exp -> io.droidevs.calculatorplus.domain.result.Result.Success(exp.evaluate()) },
                onFailure = { err -> io.droidevs.calculatorplus.domain.result.Result.Error(err) }
            )
        } catch (t: Throwable) {
            io.droidevs.calculatorplus.domain.result.Result.Error(io.droidevs.calculatorplus.domain.result.errors.InternalError(t))
        }
    }

}