package io.droidevs.calculatorplus.ui.state

import androidx.compose.runtime.Stable

@Stable
data class CalculatorState(
    val expression: String = "",
    val result: String = "0",
    val cursorPosition: Int = 0,
    val errorMessage: String? = null
)
