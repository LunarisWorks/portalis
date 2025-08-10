package io.github.lunarisworks.portalis.server.plugins

import io.github.lunarisworks.portalis.server.infrastructure.security.JwtConfig
import io.github.lunarisworks.portalis.server.infrastructure.security.JwtService
import io.github.lunarisworks.portalis.server.infrastructure.security.configureJwtAuthentication
import io.github.lunarisworks.portalis.server.infrastructure.security.createPasswordEncoder
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies
import org.springframework.security.crypto.password.PasswordEncoder

fun Application.configureSecurity() {
    dependencies {
        provide<PasswordEncoder> { createPasswordEncoder() }
        provide<JwtService> {
            JwtService(this@configureSecurity.property<JwtConfig>("security.jwt"))
        }
    }

    configureJwtAuthentication()
}
