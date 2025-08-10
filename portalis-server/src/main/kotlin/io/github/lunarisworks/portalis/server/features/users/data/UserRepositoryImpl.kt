package io.github.lunarisworks.portalis.server.features.users.data

import io.github.lunarisworks.portalis.server.features.users.domain.User
import io.github.lunarisworks.portalis.server.features.users.domain.UserRepository
import io.github.lunarisworks.portalis.server.infrastructure.database.UserSchema
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

internal class UserRepositoryImpl : UserRepository {
    override fun find(id: Uuid): User? =
        UserSchema
            .selectAll()
            .where { UserSchema.id eq id.toJavaUuid() }
            .map { rowToModel(it) }
            .firstOrNull()

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
