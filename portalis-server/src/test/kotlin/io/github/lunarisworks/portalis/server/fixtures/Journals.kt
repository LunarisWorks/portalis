package io.github.lunarisworks.portalis.server.fixtures

import io.github.lunarisworks.portalis.server.features.journals.domain.Journal
import io.github.lunarisworks.portalis.server.infrastructure.database.JournalSchema
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.insert
import kotlin.uuid.toJavaUuid

object Journals {
    val TestJournal1: Journal =
        Journal::class
            .giveMeBuilder()
            .set(Journal::userId, Users.TestUser1.id)
            .sample()
    val JournalLists: List<Journal> =
        Journal::class
            .giveMeBuilder()
            .set(Journal::userId, Users.TestUser1.id)
            .sampleList(10)

    fun init() {
        JournalSchema.insert {
            it[id] = TestJournal1.id.toJavaUuid()
            it[userId] = TestJournal1.userId.toJavaUuid()
            it[title] = TestJournal1.title
            it[content] = TestJournal1.content
            it[moodLevel] = TestJournal1.moodLevel
            it[createdAt] = TestJournal1.createdAt
            it[updatedAt] = TestJournal1.updatedAt
        }
        JournalSchema.batchInsert(JournalLists) {
            this[JournalSchema.id] = it.id.toJavaUuid()
            this[JournalSchema.userId] = it.userId.toJavaUuid()
            this[JournalSchema.title] = it.title
            this[JournalSchema.content] = it.content
            this[JournalSchema.moodLevel] = it.moodLevel
            this[JournalSchema.createdAt] = it.createdAt
            this[JournalSchema.updatedAt] = it.updatedAt
        }
    }
}
