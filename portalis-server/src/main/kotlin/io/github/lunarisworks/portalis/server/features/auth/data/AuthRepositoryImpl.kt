package io.github.lunarisworks.portalis.server.features.auth.data

import io.github.lunarisworks.portalis.server.features.auth.domain.AuthRepository
import io.github.lunarisworks.portalis.server.features.auth.domain.UserRegistration
import io.github.lunarisworks.portalis.server.features.users.domain.User
import io.github.lunarisworks.portalis.server.infrastructure.database.RefreshTokenSchema
import io.github.lunarisworks.portalis.server.infrastructure.database.UserSchema
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

internal class AuthRepositoryImpl : AuthRepository {
    override fun existsUser(
        username: String,
        email: String,
    ): Boolean =
        UserSchema
            .select(UserSchema.id)
            .where {
                (UserSchema.username eq username) or (UserSchema.email eq email)
            }.firstOrNull() != null

    override fun registerUser(data: UserRegistration): User =
        UserSchema
            .insertReturning {
                it[username] = data.username
                it[email] = data.email
                it[name] = data.name
                it[passwordHash] = data.password
            }.map { rowToModel(it) }
            .first()

    override fun findUserWithPassword(username: String): Pair<User, String?>? =
        UserSchema
            .selectAll()
            .where { UserSchema.username eq username }
            .map { rowToModel(it) to it[UserSchema.passwordHash] }
            .firstOrNull()

    override fun isRefreshTokenExists(token: String): Boolean =
        RefreshTokenSchema
            .select(RefreshTokenSchema.id)
            .where { RefreshTokenSchema.token eq token }
            .firstOrNull() != null

    override fun insertRefreshToken(
        userId: Uuid,
        token: String,
    ) {
        RefreshTokenSchema.insert {
            it[RefreshTokenSchema.userId] = userId.toJavaUuid()
            it[RefreshTokenSchema.token] = token
        }
    }

    fun rowToModel(row: ResultRow): User =
        User(
            id = row[UserSchema.id].value.toKotlinUuid(),
            username = row[UserSchema.username],
            email = row[UserSchema.email],
            name = row[UserSchema.name],
            createdAt = row[UserSchema.createdAt],
            updatedAt = row[UserSchema.updatedAt],
        )
}
