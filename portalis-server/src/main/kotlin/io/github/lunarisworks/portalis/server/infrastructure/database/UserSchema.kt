package io.github.lunarisworks.portalis.server.infrastructure.database

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object UserSchema : UUIDTable("users") {
    val username = varchar("username", 32).uniqueIndex()
    val email = varchar("email", 64).uniqueIndex()
    val name = varchar("name", 64).nullable()
    val passwordHash = varchar("password_hash", 128).nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}
