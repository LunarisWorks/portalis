package io.github.lunarisworks.portalis.server.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.lunarisworks.portalis.server.features.users.domain.User
import kotlin.time.toJavaInstant

internal const val USERNAME_CLAIM = "username"
internal const val EMAIL_CLAIM = "email"
internal const val NAME_CLAIM = "name"

class JwtService(
    private val jwtConfig: JwtConfig,
) {
    fun generateAccessToken(user: User): String =
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
}
