package io.github.lunarisworks.portalis.server.plugins

import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.github.lunarisworks.portalis.shared.auth.validateLoginRequest
import io.github.lunarisworks.portalis.shared.auth.validateRegisterRequest
import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import io.github.lunarisworks.portalis.shared.journal.validateCreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.validatePatchJournalRequest
import io.konform.validation.Validation
import io.konform.validation.ValidationError
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.Hook
import io.ktor.server.application.RouteScopedPlugin
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.request.ApplicationReceivePipeline
import kotlin.reflect.KClass

fun Application.configureRequestValidation() {
    install(RequestValidation)
}

private val validators =
    mapOf<KClass<*>, Validation<*>>(
        LoginRequest::class to validateLoginRequest,
        RegisterRequest::class to validateRegisterRequest,
        CreateJournalRequest::class to validateCreateJournalRequest,
        PatchJournalRequest::class to validatePatchJournalRequest,
    )

private val RequestValidation: RouteScopedPlugin<Unit> =
    createRouteScopedPlugin("RequestValidation") {
        on(RequestBodyTransformed) { content ->
            @Suppress("UNCHECKED_CAST")
            val errors =
                validators
                    .filterKeys { it.isInstance(content) }
                    .map { it.value as Validation<Any> }
                    .flatMap { it.validate(content).errors }
            if (errors.isNotEmpty()) {
                throw RequestValidationException(content, errors)
            }
        }
    }

private object RequestBodyTransformed : Hook<suspend (content: Any) -> Unit> {
    override fun install(
        pipeline: ApplicationCallPipeline,
        handler: suspend (content: Any) -> Unit,
    ) {
        pipeline.receivePipeline.intercept(ApplicationReceivePipeline.After) {
            handler(subject)
        }
    }
}

internal class RequestValidationException(
    val value: Any,
    val errors: List<ValidationError>,
) : IllegalArgumentException("Request validation failed")
