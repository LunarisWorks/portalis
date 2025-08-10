package io.github.lunarisworks.portalis.shared.user

import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class UserResponse(
    val id: Uuid,
    val username: String,
    val email: String,
    val name: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant,
)
