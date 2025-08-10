package io.github.lunarisworks.portalis.server.core

import io.github.lunarisworks.portalis.shared.core.errors.ApiError
import kotlin.uuid.Uuid

// todo move feature errors to their respective packages
sealed interface DomainError {
    data class UserNotFound(
        val id: Uuid,
    ) : DomainError

    class JournalNotFound(
        val id: Uuid,
    ) : DomainError

    data object UserAlreadyExists : DomainError

    data object InvalidCredentials : DomainError

    data object PasswordNotSet : DomainError
}

fun DomainError.toApiError(): ApiError =
    when (this) {
        DomainError.InvalidCredentials -> ApiError.InvalidCredentials()
        is DomainError.JournalNotFound -> ApiError.ResourceNotFound("Journal with ID $id not found")
        DomainError.PasswordNotSet -> ApiError.PasswordNotSet
        DomainError.UserAlreadyExists -> ApiError.ResourceAlreadyExists("User already exists")
        is DomainError.UserNotFound -> ApiError.ResourceNotFound("User with ID $id not found")
    }
