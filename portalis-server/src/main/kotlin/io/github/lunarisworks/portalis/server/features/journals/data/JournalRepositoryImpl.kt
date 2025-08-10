package io.github.lunarisworks.portalis.server.features.journals.data

import io.github.lunarisworks.portalis.server.core.CursorPaged
import io.github.lunarisworks.portalis.server.features.journals.domain.Journal
import io.github.lunarisworks.portalis.server.features.journals.domain.JournalPatch
import io.github.lunarisworks.portalis.server.features.journals.domain.JournalRepository
import io.github.lunarisworks.portalis.server.features.journals.domain.NewJournal
import io.github.lunarisworks.portalis.server.infrastructure.database.JournalSchema
import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.statements.UpsertSqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insertReturning
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.updateReturning
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

class JournalRepositoryImpl : JournalRepository {
    override fun list(
        userId: Uuid,
        cursor: String?,
        limit: Int,
    ): CursorPaged<Journal> {
        val query =
            JournalSchema
                .selectAll()
                .where { userIdEq(userId) }
                .limit(limit + 1)
                .orderBy(JournalSchema.createdAt, SortOrder.DESC)

        if (cursor != null) {
            query.andWhere {
                JournalSchema.createdAt less Instant.parse(cursor.decodeBase64String())
            }
        }

        val content =
            query
                .map { rowToModel(it) }
        val nextCursor =
            if (content.size > limit) {
                content
                    .last()
                    .createdAt
                    .toString()
                    .encodeBase64()
            } else {
                null
            }
        return CursorPaged(
            items = content.take(limit),
            nextCursor = nextCursor,
        )
    }

    override fun find(
        userId: Uuid,
        id: Uuid,
    ): Journal? =
        JournalSchema
            .selectAll()
            .where { idAndUserIdEq(id, userId) }
            .map { rowToModel(it) }
            .firstOrNull()

    private fun idAndUserIdEq(
        id: Uuid,
        userId: Uuid,
    ) = idEq(id) and userIdEq(userId)

    private fun idEq(id: Uuid) = JournalSchema.id eq id.toJavaUuid()

    private fun userIdEq(userId: Uuid) = JournalSchema.userId eq userId.toJavaUuid()

    override fun update(
        userId: Uuid,
        id: Uuid,
        data: JournalPatch,
    ): Journal? =
        update0(
            id = id,
            model = data,
            userId = userId,
        )

    private fun update0(
        id: Uuid,
        model: JournalPatch,
        userId: Uuid? = null,
    ): Journal? =
        JournalSchema
            .updateReturning(
                where = { if (userId != null) idAndUserIdEq(id, userId) else idEq(id) },
            ) { row ->
                model.title?.let { row[JournalSchema.title] = it }
                model.content?.let { row[JournalSchema.content] = it }
                model.moodLevel?.let { row[JournalSchema.moodLevel] = it }
                model.createdAt?.let { row[JournalSchema.createdAt] = it }
                row[JournalSchema.updatedAt] = Clock.System.now()
            }.map { rowToModel(it) }
            .firstOrNull()

    override fun delete(
        userId: Uuid,
        id: Uuid,
    ): Boolean =
        softDelete(
            id = id,
            userId = userId,
        )

    private fun softDelete(
        id: Uuid,
        userId: Uuid? = null,
    ): Boolean =
        JournalSchema
            .updateReturning(
                where = { if (userId != null) idAndUserIdEq(id, userId) else idEq(id) },
            ) { row ->
                row[JournalSchema.deleteAt] = Clock.System.now()
            }.count() > 0

    override fun insert(
        userId: Uuid,
        data: NewJournal,
    ): Journal =
        JournalSchema
            .insertReturning { row ->
                row[JournalSchema.userId] = userId.toJavaUuid()
                row[title] = data.title
                row[content] = data.content
                row[moodLevel] = data.moodLevel
                data.createdAt?.let { row[createdAt] = it }
            }.map { rowToModel(it) }
            .first()

    fun rowToModel(row: ResultRow): Journal =
        Journal(
            id = row[JournalSchema.id].value.toKotlinUuid(),
            userId = row[JournalSchema.userId].toKotlinUuid(),
            title = row[JournalSchema.title],
            content = row[JournalSchema.content],
            moodLevel = row[JournalSchema.moodLevel],
            createdAt = row[JournalSchema.createdAt],
            updatedAt = row[JournalSchema.updatedAt],
        )
}
