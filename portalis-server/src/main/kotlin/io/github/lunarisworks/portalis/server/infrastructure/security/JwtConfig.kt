package io.github.lunarisworks.portalis.server.infrastructure.security

import io.github.lunarisworks.portalis.server.infrastructure.serialization.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration

@Serializable
data class JwtConfig(
    val realm: String,
    val issuer: String,
    val audience: String,
    @SerialName("expires-in")
    @Serializable(with = DurationSerializer::class)
    val expiresIn: Duration,
    val secret: String,
) {
    fun expiresAt() = Clock.System.now() + expiresIn
}
