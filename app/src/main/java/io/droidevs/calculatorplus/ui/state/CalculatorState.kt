package io.droidevs.calculatorplus.ui.state

import androidx.compose.runtime.Stable
import java.math.BigDecimal


@Stable
class CalculatorState(
    val cursorPosition: Int,
    val expression: String,
    val result: BigDecimal
)