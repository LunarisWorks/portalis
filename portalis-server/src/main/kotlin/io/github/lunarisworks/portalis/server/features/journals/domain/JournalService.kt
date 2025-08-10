package io.github.lunarisworks.portalis.server.features.journals.domain

import io.github.lunarisworks.portalis.server.core.CursorPaged
import io.github.lunarisworks.portalis.server.core.DomainError
import io.github.lunarisworks.portalis.server.core.ServiceResult
import io.github.lunarisworks.portalis.server.core.asFailure
import io.github.lunarisworks.portalis.server.core.asSuccess
import io.github.lunarisworks.portalis.server.infrastructure.database.dbQuery
import kotlin.uuid.Uuid

class JournalService(
    private val journalRepository: JournalRepository,
) {
    suspend fun create(
        userId: Uuid,
        data: NewJournal,
    ): ServiceResult<Journal> =
        dbQuery {
            journalRepository
                .insert(userId, data)
                .asSuccess()
        }

    suspend fun read(
        userId: Uuid,
        id: Uuid,
    ): ServiceResult<Journal> =
        dbQuery {
            journalRepository
                .find(userId, id)
                ?.asSuccess()
                ?: DomainError.JournalNotFound(id).asFailure()
        }

    suspend fun list(
        userId: Uuid,
        cursor: String?,
        limit: Int,
    ): ServiceResult<CursorPaged<Journal>> =
        dbQuery {
            journalRepository
                .list(userId, cursor, limit)
                .asSuccess()
        }

    suspend fun patch(
        userId: Uuid,
        id: Uuid,
        data: JournalPatch,
    ): ServiceResult<Journal> =
        dbQuery {
            journalRepository
                .update(userId, id, data)
                ?.asSuccess()
                ?: DomainError.JournalNotFound(id).asFailure()
        }

    suspend fun delete(
        userId: Uuid,
        id: Uuid,
    ): ServiceResult<Unit> =
        dbQuery {
            if (journalRepository.delete(userId, id)) {
                Unit.asSuccess()
            } else {
                DomainError.JournalNotFound(id).asFailure()
            }
        }
}
