package io.github.lunarisworks.portalis.client

import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.core.CursorPageResponse
import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.JournalResponse
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import kotlin.uuid.Uuid

interface JournalApi {
    suspend fun list(
        cursor: String? = null,
        limit: Int? = null,
    ): ApiResult<CursorPageResponse<JournalResponse>>

    suspend fun get(id: Uuid): ApiResult<JournalResponse>

    suspend fun create(request: CreateJournalRequest): ApiResult<JournalResponse>

    suspend fun patch(
        id: Uuid,
        request: PatchJournalRequest,
    ): ApiResult<JournalResponse>

    suspend fun delete(id: Uuid): ApiResult<Unit>
}
