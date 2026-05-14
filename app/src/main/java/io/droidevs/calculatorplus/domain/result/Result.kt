package io.droidevs.calculatorplus.domain.result

import io.droidevs.calculatorplus.domain.result.errors.AppError


sealed interface Result<out T> {

    class Success<T>(val result: T) : Result<T>

    class Error(val error : AppError) : Result<Nothing>
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(result)
    return this
}
fun <D> Result<D>.onSuccessWithResult(action: (D) -> Result<D>): Result<D> =
    if (this is Result.Success) action(this.result) else this

inline fun <T> Result<T>.onFailure(action: (AppError) -> Unit): Result<T> {
    if (this is Result.Error) action(error)
    return this
}

fun <D> Result<D>.onFailureWithResult(action: (AppError) -> Result<D>): Result<D> =
    if (this is Result.Error) action(this.error) else this

inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(result)
        is Result.Error -> onFailure(error)
    }
}
