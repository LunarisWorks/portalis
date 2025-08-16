package io.github.lunarisworks.portalis.server.features.auth.domain

import io.github.lunarisworks.portalis.server.features.users.domain.User
import kotlin.uuid.Uuid

interface AuthRepository {
    fun existsUser(
        username: String,
        email: String,
    ): Boolean

    fun registerUser(data: UserRegistration): User

    fun findUserWithPassword(username: String): Pair<User, String?>?

    fun isRefreshTokenExists(token: String): Boolean

    fun insertRefreshToken(
        userId: Uuid,
        token: String,
    )
}
