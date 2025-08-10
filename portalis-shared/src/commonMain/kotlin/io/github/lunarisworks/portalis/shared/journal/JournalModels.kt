package io.github.lunarisworks.portalis.shared.journal

import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
data class JournalResponse(
    val id: Uuid,
    val userId: Uuid,
    val title: String?,
    val content: String,
    val moodLevel: Int?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

@Serializable
data class CreateJournalRequest(
    val content: String,
    val title: String? = null,
    val moodLevel: Int? = null,
    val createdAt: Instant? = null,
)

@Serializable
data class PatchJournalRequest(
    val title: String? = null,
    val content: String? = null,
    val moodLevel: Int? = null,
    val createdAt: Instant? = null,
)
