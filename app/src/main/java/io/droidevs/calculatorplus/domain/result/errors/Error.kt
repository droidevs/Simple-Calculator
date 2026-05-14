package io.droidevs.calculatorplus.domain.result.errors

sealed interface AppError


class InvalidPositionError : AppError

class InvalidOperatorInPositionError : AppError

class InvalidExpressionFormat : AppError

data class InternalError(val cause: Throwable) : AppError

data class UnknownError(val cause: Throwable) : AppError