package io.github.lunarisworks.portalis.server.features.auth.presentation

import io.github.lunarisworks.portalis.server.core.respondTo
import io.github.lunarisworks.portalis.server.core.toApiResult
import io.github.lunarisworks.portalis.server.features.auth.domain.AuthService
import io.github.lunarisworks.portalis.server.features.auth.domain.toModel
import io.github.lunarisworks.portalis.server.features.auth.domain.toResponse
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RefreshTokenRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.routing.Route

fun Route.authRoutes(authService: AuthService) {
    post<Routes.Auth.Register> {
        val body = call.receive<RegisterRequest>()
        authService
            .register(body.toModel())
            .toApiResult()
            .respondTo(call, HttpStatusCode.Created)
    }
    post<Routes.Auth.Login> {
        val body = call.receive<LoginRequest>()
        authService
            .login(body.toModel())
            .toApiResult { it.toResponse() }
            .respondTo(call)
    }

    post<Routes.Auth.Refresh> {
        val body = call.receive<RefreshTokenRequest>()
        authService
            .refresh(body.toModel())
            .toApiResult { it.toResponse() }
            .respondTo(call)
    }
}
