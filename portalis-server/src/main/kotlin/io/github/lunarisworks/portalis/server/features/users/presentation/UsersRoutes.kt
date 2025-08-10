package io.github.lunarisworks.portalis.server.features.users.presentation

import io.github.lunarisworks.portalis.server.core.respondTo
import io.github.lunarisworks.portalis.server.core.toApiResult
import io.github.lunarisworks.portalis.server.features.users.domain.UserService
import io.github.lunarisworks.portalis.server.features.users.domain.toResponse
import io.github.lunarisworks.portalis.server.infrastructure.security.principal
import io.github.lunarisworks.portalis.shared.Routes
import io.ktor.server.resources.get
import io.ktor.server.routing.Route

fun Route.userRoutes(userService: UserService) {
    get<Routes.Users.Me> {
        val principal = call.principal()
        userService
            .get(principal.userId)
            .toApiResult {
                it.toResponse()
            }.respondTo(call)
    }
}
