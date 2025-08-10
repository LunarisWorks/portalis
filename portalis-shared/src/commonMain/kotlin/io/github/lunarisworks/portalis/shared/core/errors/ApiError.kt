package io.github.lunarisworks.portalis.shared.core.errors

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Polymorphic
interface ApiError {
    val status: Int
    val message: String

    @Serializable
    data object UndefinedError : ApiError {
        override val status: Int = 500
        override val message: String = "An undefined error occurred."
    }

    @Serializable
    @SerialName("ResourceNotFound")
    data class ResourceNotFound(
        override val message: String = "Resource not found.",
    ) : ApiError {
        override val status: Int = 404
    }

    @Serializable
    @SerialName("ResourceAlreadyExists")
    data class ResourceAlreadyExists(
        override val message: String = "Resource already exists.",
    ) : ApiError {
        override val status: Int = 409
    }

    @Serializable
    @SerialName("InvalidCredentials")
    @Polymorphic
    data class InvalidCredentials(
        override val message: String = "Invalid credentials provided.",
    ) : ApiError {
        override val status: Int = 401
    }

    @Serializable
    @SerialName("PasswordNotSet")
    data object PasswordNotSet : ApiError {
        override val status: Int = 403
        override val message: String = "Password not set."
    }

    @Serializable
    @SerialName("ValidationError")
    data class ValidationError(
        val details: List<FieldError>,
        override val message: String = "Request contains invalid fields: ${
            details.distinctBy { it.path }.joinToString { it.path }
        }",
    ) : ApiError {
        override val status: Int = 400
    }
}
