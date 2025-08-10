package io.github.lunarisworks.portalis.shared.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val name: String? = null,
)

@Serializable
data class TokensResponse(
    val accessToken: String,
)
