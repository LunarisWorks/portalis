package io.github.lunarisworks.portalis.server.plugins

import io.github.lunarisworks.portalis.shared.core.errors.ApiError
import io.github.lunarisworks.portalis.shared.core.errors.FieldError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.util.reflect.typeInfo

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            val apiError =
                ApiError.ValidationError(
                    cause.errors.map { error ->
                        FieldError(
                            path = error.dataPath.removePrefix("."),
                            message = error.message,
                        )
                    },
                )
            call.respond(HttpStatusCode.BadRequest, apiError, typeInfo<ApiError>())
        }
    }
}
