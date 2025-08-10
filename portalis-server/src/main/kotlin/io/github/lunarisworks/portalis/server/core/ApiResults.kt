package io.github.lunarisworks.portalis.server.core

import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.core.errors.ApiError
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> io.github.lunarisworks.portalis.shared.core.ApiResult<T>.respondTo(
    call: RoutingCall,
    successStatus: HttpStatusCode = HttpStatusCode.OK,
) {
    when (this) {
        is ApiResult.Success -> {
            if (value !is Unit) {
                call.respond(successStatus, value, typeInfo<T>())
            } else {
                call.respond(successStatus)
            }
        }

        is ApiResult.Failure ->
            call.respond(
                HttpStatusCode.fromValue(error.status),
                error,
                typeInfo<ApiError>(),
            )
    }
}
