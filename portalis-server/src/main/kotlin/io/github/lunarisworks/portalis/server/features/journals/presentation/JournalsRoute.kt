package io.github.lunarisworks.portalis.server.features.journals.presentation

import io.github.lunarisworks.portalis.server.core.respondTo
import io.github.lunarisworks.portalis.server.core.toApiResult
import io.github.lunarisworks.portalis.server.core.toResponse
import io.github.lunarisworks.portalis.server.features.journals.domain.JournalService
import io.github.lunarisworks.portalis.server.features.journals.domain.toModel
import io.github.lunarisworks.portalis.server.features.journals.domain.toResponse
import io.github.lunarisworks.portalis.server.infrastructure.security.principal
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.href
import io.ktor.server.resources.patch
import io.ktor.server.resources.post
import io.ktor.server.response.header
import io.ktor.server.routing.Route

fun Route.journalsRoutes(journalService: JournalService) {
    post<Routes.Journals.Create> {
        val principal = call.principal()
        val body = call.receive<CreateJournalRequest>()

        journalService
            .create(principal.userId, body.toModel())
            .toApiResult {
                call.response.header(
                    HttpHeaders.Location,
                    call.application.href(Routes.Journals.Get(id = it.id)),
                )
                it.toResponse()
            }.respondTo(call, HttpStatusCode.Created)
    }

    get<Routes.Journals.Get> { params ->
        val principal = call.principal()

        journalService
            .read(principal.userId, params.id)
            .toApiResult { it.toResponse() }
            .respondTo(call)
    }

    get<Routes.Journals.List> { params ->
        val principal = call.principal()
        journalService
            .list(
                userId = principal.userId,
                cursor = params.cursor,
                limit = params.limit,
            ).toApiResult { it.toResponse { journal -> journal.toResponse() } }
            .respondTo(call)
    }

    patch<Routes.Journals.Patch> { params ->
        val principal = call.principal()
        val body = call.receive<PatchJournalRequest>()
        journalService
            .patch(principal.userId, params.id, body.toModel())
            .toApiResult { it.toResponse() }
            .respondTo(call)
    }

    delete<Routes.Journals.Delete> {
        val principal = call.principal()
        journalService
            .delete(principal.userId, it.id)
            .toApiResult()
            .respondTo(call, HttpStatusCode.NoContent)
    }
}
