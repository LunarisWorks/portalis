package io.github.lunarisworks.portalis.server.features.journals.domain

import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.JournalResponse
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Journal(
    val id: Uuid,
    val userId: Uuid,
    val title: String?,
    val content: String,
    val moodLevel: Int?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class NewJournal(
    val content: String,
    val title: String? = null,
    val moodLevel: Int? = null,
    val createdAt: Instant? = null,
)

data class JournalPatch(
    val title: String? = null,
    val content: String? = null,
    val moodLevel: Int? = null,
    val createdAt: Instant? = null,
)

fun CreateJournalRequest.toModel() =
    NewJournal(
        content = content,
        title = title,
        moodLevel = moodLevel,
        createdAt = createdAt,
    )

fun PatchJournalRequest.toModel() =
    JournalPatch(
        title = title,
        content = content,
        moodLevel = moodLevel,
        createdAt = createdAt,
    )

fun Journal.toResponse() =
    JournalResponse(
        id = id,
        userId = userId,
        title = title,
        content = content,
        moodLevel = moodLevel,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
