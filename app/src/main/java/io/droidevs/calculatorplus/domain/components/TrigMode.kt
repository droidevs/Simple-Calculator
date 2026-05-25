package io.droidevs.calculatorplus.domain.components

// BUG FIX #3: TrigMode controls whether trigonometric functions interpret
// their argument as Radians or Degrees. RadAction and DegreeAction existed
// in the UI but were never wired to anything — this type closes the gap.
enum class TrigMode {
    RADIANS,
    DEGREES;

    fun toggle(): TrigMode = if (this == RADIANS) DEGREES else RADIANS
}