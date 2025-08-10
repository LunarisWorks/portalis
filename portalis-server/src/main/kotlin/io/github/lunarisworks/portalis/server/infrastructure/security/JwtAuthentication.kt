package io.github.lunarisworks.portalis.server.infrastructure.security

import com.auth0.jwt.RegisteredClaims
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.github.lunarisworks.portalis.shared.core.errors.ApiError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPayloadHolder
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.property
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.util.reflect.typeInfo
import kotlin.uuid.Uuid

private const val AUTH_JWT = "auth-jwt"

fun Routing.authenticate(build: Route.() -> Unit) =
    authenticate(AUTH_JWT) {
        build()
    }

fun ApplicationCall.principal(): UserPrincipal {
    val principal = authentication.principal<UserPrincipal>(AUTH_JWT)
    check(principal != null) {
        "Technical error: Please wrap your call in an authentication block."
    }
    return principal
}

fun Application.configureJwtAuthentication() {
    val config = property<JwtConfig>("security.jwt")
    authentication {
        jwt(AUTH_JWT) {
            realm = config.realm
            verifier(
                issuer = config.issuer,
                audience = config.audience,
                algorithm = Algorithm.HMAC256(config.secret),
            ) {
                withClaimPresence(RegisteredClaims.SUBJECT)
                withClaimPresence(USERNAME_CLAIM)
                withClaimPresence(EMAIL_CLAIM)
            }
            validate { credential -> UserPrincipal(credential.payload) }
            challenge { defaultScheme, realm ->
                call.response.header(HttpHeaders.WWWAuthenticate, "$defaultScheme realm=\"$realm\"")
                call.respond(HttpStatusCode.Unauthorized, ApiError.InvalidCredentials(), typeInfo<ApiError>())
            }
        }
    }
}

class UserPrincipal(
    payload: Payload,
) : JWTPayloadHolder(payload) {
    val userId =
        Uuid.parse(
            checkNotNull(subject) {
                "JWT subject cannot be null"
            },
        )
    val username =
        checkNotNull(this[USERNAME_CLAIM]) {
            "JWT username claim cannot be null"
        }
    val email =
        checkNotNull(this[EMAIL_CLAIM]) {
            "JWT email claim cannot be null"
        }
    val name = this[NAME_CLAIM]
}
