package io.github.lunarisworks.portalis.server.features.auth

import io.github.lunarisworks.portalis.server.features.auth.data.AuthRepositoryImpl
import io.github.lunarisworks.portalis.server.features.auth.domain.AuthRepository
import io.github.lunarisworks.portalis.server.features.auth.domain.AuthService
import io.github.lunarisworks.portalis.server.features.auth.presentation.authRoutes
import io.github.lunarisworks.portalis.server.infrastructure.security.JwtConfig
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.routing

fun Application.authModule() {
    dependencies {
        provide<AuthRepository> { AuthRepositoryImpl() }
        provide<AuthService> {
            val jwtConfig = this@authModule.property<JwtConfig>("security.jwt")
            AuthService(resolve(), jwtConfig, resolve(), resolve())
        }
    }

    val authService: AuthService by dependencies
    routing {
        authRoutes(authService)
    }
}
