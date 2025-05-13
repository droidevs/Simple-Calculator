package io.droidevs.calculatorplus.domain.model

import io.droidevs.calculatorplus.domain.result.AppError
import java.math.BigDecimal

data class Calculation(
    val expression: String,
    val result: BigDecimal,
    val error: AppError? = null
)