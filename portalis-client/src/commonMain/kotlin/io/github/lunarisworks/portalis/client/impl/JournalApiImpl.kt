package io.github.lunarisworks.portalis.client.impl

import io.github.lunarisworks.portalis.client.JournalApi
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.core.CursorPageParams
import io.github.lunarisworks.portalis.shared.core.CursorPageResponse
import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.JournalResponse
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.setBody
import kotlin.uuid.Uuid

internal class JournalApiImpl(
    private val httpClient: HttpClient,
) : JournalApi {
    override suspend fun list(
        cursor: String?,
        limit: Int?,
    ): ApiResult<CursorPageResponse<JournalResponse>> =
        httpClient
            .get(Routes.Journals.List(cursor = cursor, limit = limit ?: CursorPageParams.DEFAULT_LIMIT))
            .toApiResult()

    override suspend fun get(id: Uuid): ApiResult<JournalResponse> =
        httpClient
            .get(Routes.Journals.Get(id = id))
            .toApiResult()

    override suspend fun create(request: CreateJournalRequest): ApiResult<JournalResponse> =
        httpClient
            .get(Routes.Journals.Create()) {
                setBody(request)
            }.toApiResult()

    override suspend fun patch(
        id: Uuid,
        request: PatchJournalRequest,
    ): ApiResult<JournalResponse> =
        httpClient
            .get(Routes.Journals.Patch(id = id)) {
                setBody(request)
            }.toApiResult()

    override suspend fun delete(id: Uuid): ApiResult<Unit> =
        httpClient
            .get(Routes.Journals.Delete(id = id))
            .toApiResult()
}
