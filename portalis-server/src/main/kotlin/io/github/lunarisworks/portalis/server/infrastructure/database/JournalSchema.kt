package io.github.lunarisworks.portalis.server.infrastructure.database

import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

object JournalSchema : UUIDTable("journal_entries") {
    val userId = uuid("user_id").references(UserSchema.id).index()
    val title = varchar("title", 255).nullable()
    val content = text("content")
    val moodLevel = integer("mood_level").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
    val deleteAt = timestamp("deleted_at").nullable()
}
