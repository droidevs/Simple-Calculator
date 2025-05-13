package io.droidevs.calculatorplus.domain.result


interface AppError

class InvalidPositionError : AppError

class InvalidOperatorInPositionError : AppError


sealed interface Result<out T> {

    class Success<T>(val result: T) : Result<T>

    class Error(val error : AppError) : Result<Nothing>
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(result)
    return this
}

inline fun <T> Result<T>.onFailure(action: (AppError) -> Unit): Result<T> {
    if (this is Result.Error) action(error)
    return this
}

inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(result)
        is Result.Error -> onFailure(error)
        else -> { throw IllegalStateException("Invalid Result type") }
    }
}
