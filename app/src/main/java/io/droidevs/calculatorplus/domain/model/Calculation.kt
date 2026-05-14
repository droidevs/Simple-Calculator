package io.droidevs.calculatorplus.domain.model


import io.droidevs.calculatorplus.domain.result.errors.AppError
import io.droidevs.calculatorplus.domain.token.LinkedToken
import io.droidevs.calculatorplus.domain.token.SpecialToken
import java.math.BigDecimal

data class Calculation(
    val tokens: LinkedToken = SpecialToken.EmptyToken.get().apply { startIndex = 0 },
    val expression: String = "",
    val pos: Int = 0,
    val result: BigDecimal = BigDecimal.ZERO,
    val error: AppError? = null
)