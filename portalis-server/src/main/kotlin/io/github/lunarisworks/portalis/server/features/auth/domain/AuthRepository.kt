package io.github.lunarisworks.portalis.server.features.auth.domain

import io.github.lunarisworks.portalis.server.features.users.domain.User
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class RefreshTokenInfo(
    val id: Uuid,
    val user: User,
    val createdAt: Instant,
)

interface AuthRepository {
    fun existsUser(
        username: String,
        email: String,
    ): Boolean

    fun registerUser(data: UserRegistration): User

    fun findUserWithPassword(username: String): Pair<User, String?>?

    fun existsRefreshToken(token: String): Boolean

    fun findRefreshToken(token: String): RefreshTokenInfo?

    fun revokeRefreshToken(id: Uuid)

    fun insertRefreshToken(
        userId: Uuid,
        token: String,
    )
}
