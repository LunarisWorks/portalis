package io.github.lunarisworks.portalis.server.infrastructure.database

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object RefreshTokenSchema : UUIDTable("refresh_tokens") {
    val userId = uuid("user_id").references(UserSchema.id).index()
    val token = varchar("token", 128).uniqueIndex()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val revokedAt = timestamp("revoked_at").nullable()
}
