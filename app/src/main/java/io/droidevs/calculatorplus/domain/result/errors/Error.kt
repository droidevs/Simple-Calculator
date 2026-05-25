package io.droidevs.calculatorplus.domain.result.errors

sealed interface AppError

class InvalidPositionError : AppError

class InvalidOperatorInPositionError : AppError

class InvalidExpressionFormat : AppError

// BUG FIX #2 (new): Dedicated error for division by zero so the UI can show
// a specific "Division by zero" message instead of the generic "Invalid expression".
class DivisionByZeroError : AppError

// BUG FIX #22 (new): Dedicated error for math domain errors (sqrt of negative, etc.)
class MathDomainError(val message: String) : AppError

data class InternalError(val cause: Throwable) : AppError

data class UnknownError(val cause: Throwable) : AppError