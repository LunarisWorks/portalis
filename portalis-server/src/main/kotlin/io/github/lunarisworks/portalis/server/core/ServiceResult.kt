package io.github.lunarisworks.portalis.server.core

import io.github.lunarisworks.portalis.shared.core.ApiResult

sealed interface ServiceResult<out T> {
    @JvmInline
    value class Success<T>(
        val value: T,
    ) : ServiceResult<T>

    @JvmInline
    value class Failure(
        val error: DomainError,
    ) : ServiceResult<Nothing>
}

fun <T> T.asSuccess() = ServiceResult.Success(this)

fun DomainError.asFailure(): ServiceResult<Nothing> = ServiceResult.Failure(this)

inline fun <T, R> ServiceResult<T>.toApiResult(transform: (T) -> R): ApiResult<R> =
    when (this) {
        is ServiceResult.Success -> ApiResult.Success(transform(value))
        is ServiceResult.Failure -> ApiResult.Failure(error.toApiError())
    }

fun <T> ServiceResult<T>.toApiResult(): ApiResult<Unit> =
    when (this) {
        is ServiceResult.Success -> ApiResult.Success(Unit)
        is ServiceResult.Failure -> ApiResult.Failure(error.toApiError())
    }
