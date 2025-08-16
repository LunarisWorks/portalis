package io.github.lunarisworks.portalis.server.features.auth.domain

import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.github.lunarisworks.portalis.shared.auth.TokensResponse

data class LoginCredentials(
    val username: String,
    val password: String,
)

data class UserRegistration(
    val username: String,
    val email: String,
    val password: String,
    val name: String? = null,
)

data class RefreshTokenCredentials(
    val token: String,
)

data class AuthenticateTokens(
    val accessToken: String,
    val refreshToken: String,
)

fun LoginRequest.toModel(): LoginCredentials =
    LoginCredentials(
        username = username,
        password = password,
    )

fun RegisterRequest.toModel(): UserRegistration =
    UserRegistration(
        username = username,
        email = email,
        password = password,
        name = name,
    )

fun AuthenticateTokens.toResponse(): TokensResponse =
    TokensResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
