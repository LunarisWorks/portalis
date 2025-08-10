package io.github.lunarisworks.portalis.server.features.journals.domain

import io.github.lunarisworks.portalis.server.core.CursorPaged
import kotlin.uuid.Uuid

interface JournalRepository {
    fun list(
        userId: Uuid,
        cursor: String? = null,
        limit: Int = 20,
    ): CursorPaged<Journal>

    fun find(
        userId: Uuid,
        id: Uuid,
    ): Journal?

    fun insert(
        userId: Uuid,
        data: NewJournal,
    ): Journal

    fun update(
        userId: Uuid,
        id: Uuid,
        data: JournalPatch,
    ): Journal?

    fun delete(
        userId: Uuid,
        id: Uuid,
    ): Boolean
}
