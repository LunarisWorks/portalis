package io.github.lunarisworks.portalis.server.features.users.domain

import io.github.lunarisworks.portalis.shared.user.UserResponse
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class User(
    val id: Uuid,
    val username: String,
    val email: String,
    val name: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun User.toResponse(): UserResponse =
    UserResponse(
        id = id,
        username = username,
        email = email,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
