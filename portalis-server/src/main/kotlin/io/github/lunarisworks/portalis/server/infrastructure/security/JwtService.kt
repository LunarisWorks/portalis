package io.github.lunarisworks.portalis.server.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.lunarisworks.portalis.server.features.users.domain.User
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlin.time.toJavaInstant

@Serializable
data class AuthenticateTokens(
    val accessToken: String,
)

internal const val USERNAME_CLAIM = "username"
internal const val EMAIL_CLAIM = "email"
internal const val NAME_CLAIM = "name"

class JwtService(
    private val jwtConfig: JwtConfig,
) {
    suspend fun createToken(user: User): AuthenticateTokens =
        coroutineScope {
            val token =
                JWT
                    .create()
                    .withIssuer(jwtConfig.issuer)
                    .withAudience(jwtConfig.audience)
                    .withExpiresAt(jwtConfig.expiresAt().toJavaInstant())
                    .withSubject(user.id.toString())
                    .withClaim(USERNAME_CLAIM, user.username)
                    .withClaim(EMAIL_CLAIM, user.email)
                    .withClaim(NAME_CLAIM, user.name)
                    .sign(Algorithm.HMAC256(jwtConfig.secret))

            AuthenticateTokens(
                accessToken = token,
            )
        }
}
