package io.github.lunarisworks.portalis.shared.core

import io.github.lunarisworks.portalis.shared.core.errors.ApiError

/**
 * Represents the result of an API call.
 *
 * @param T The type of the successful result.
 */
sealed interface ApiResult<out T> {
    /** Represents a successful API call with a [value] of type [T]. */
    @JvmInline
    value class Success<out T>(
        val value: T,
    ) : ApiResult<T>

    /** Represents a failed API call with an [error]. */
    @JvmInline
    value class Failure(
        val error: ApiError,
    ) : ApiResult<Nothing>
}

/** Maps the successful value of an [ApiResult] to a new type [R]. */
inline fun <R, T> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(value))
        is ApiResult.Failure -> this
    }

/** Folds the [ApiResult] into a single value of type [R], based on whether it is a success or failure. */
inline fun <R, T> ApiResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (ApiError) -> R,
): R =
    when (this) {
        is ApiResult.Success -> onSuccess(value)
        is ApiResult.Failure -> onFailure(error)
    }

/** Executes [action] if the [ApiResult] is a success, passing the successful value to it. */
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) {
        action(value)
    }
    return this
}

/** Executes [action] if the [ApiResult] is a failure, passing the error to it. */
inline fun <T> ApiResult<T>.onFailure(action: (ApiError) -> Unit): ApiResult<T> {
    if (this is ApiResult.Failure) {
        action(error)
    }
    return this
}
