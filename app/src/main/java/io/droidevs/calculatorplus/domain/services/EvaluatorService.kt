package io.droidevs.calculatorplus.domain.services

import io.droidevs.calculatorplus.domain.result.Result
import io.droidevs.calculatorplus.domain.result.errors.DivisionByZeroError
import io.droidevs.calculatorplus.domain.result.errors.InternalError
import io.droidevs.calculatorplus.domain.result.errors.MathDomainError
import io.droidevs.calculatorplus.domain.result.fold
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.TokenProvider
import java.math.BigDecimal

class EvaluatorService {

    fun evaluate(tokens: LinkedToken): Result<BigDecimal> {
        return try {
            TokenProvider.construct(tokens).parse().fold(
                onSuccess = { exp ->
                    val value = exp.evaluate()
                    // BUG FIX #22: NaN/Infinity from Double conversion in FunctionExpression
                    // must surface as an error rather than being silently stored.
                    if (value.toDouble().isNaN() || value.toDouble().isInfinite()) {
                        Result.Error(MathDomainError("Result is undefined"))
                    } else {
                        Result.Success(value)
                    }
                },
                onFailure = { err -> Result.Error(err) }
            )
        } catch (e: ArithmeticException) {
            // BUG FIX #2: Map the ArithmeticException from BigDecimal.divide() and
            // trig domain errors to typed errors so the UI can show specific messages.
            when {
                e.message?.contains("zero", ignoreCase = true) == true ->
                    Result.Error(DivisionByZeroError())
                else ->
                    Result.Error(MathDomainError(e.message ?: "Math error"))
            }
        } catch (t: Throwable) {
            Result.Error(InternalError(t))
        }
    }
}