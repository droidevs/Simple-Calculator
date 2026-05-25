package io.droidevs.calculatorplus.ui.state

import androidx.compose.runtime.Stable
import io.droidevs.calculatorplus.domain.components.TrigMode

@Stable
data class CalculatorState(
    val expression: String = "",
    val result: String = "0",
    val cursorPosition: Int = 0,
    val errorMessage: String? = null,
    // BUG FIX #3: Expose trigMode so the UI can display the RAD/DEG badge
    // and users know which mode is active.
    val trigMode: TrigMode = TrigMode.RADIANS
)