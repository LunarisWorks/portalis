package io.github.lunarisworks.portalis.server.features.auth

import io.github.lunarisworks.portalis.server.features.auth.data.AuthRepositoryImpl
import io.github.lunarisworks.portalis.server.features.auth.domain.AuthRepository
import io.github.lunarisworks.portalis.server.features.auth.domain.AuthService
import io.github.lunarisworks.portalis.server.features.auth.presentation.authRoutes
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.routing

fun Application.authModule() {
    dependencies {
        provide<AuthRepository> { AuthRepositoryImpl() }
        provide<AuthService> { AuthService(resolve(), resolve(), resolve()) }
    }

    val authService: AuthService by dependencies
    routing {
        authRoutes(authService)
    }
}
