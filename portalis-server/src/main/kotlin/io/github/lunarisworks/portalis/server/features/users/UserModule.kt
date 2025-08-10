package io.github.lunarisworks.portalis.server.features.users

import io.github.lunarisworks.portalis.server.features.users.data.UserRepositoryImpl
import io.github.lunarisworks.portalis.server.features.users.domain.UserRepository
import io.github.lunarisworks.portalis.server.features.users.domain.UserService
import io.github.lunarisworks.portalis.server.features.users.presentation.userRoutes
import io.github.lunarisworks.portalis.server.infrastructure.security.authenticate
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.di.resolve
import io.ktor.server.routing.routing

fun Application.userModule() {
    dependencies {
        provide<UserRepository> { UserRepositoryImpl() }
        provide<UserService> { UserService(resolve()) }
    }

    val userService: UserService by dependencies

    routing {
        authenticate {
            userRoutes(userService)
        }
    }
}
