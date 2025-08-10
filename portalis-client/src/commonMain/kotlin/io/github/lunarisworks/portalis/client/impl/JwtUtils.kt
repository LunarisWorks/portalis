package io.github.lunarisworks.portalis.client.impl

import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64

private val json by lazy {
    Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
}

internal inline fun <reified T> decodeJwt(jwt: String): T {
    val parts = jwt.split('.')
    require(parts.size == 3) {
        "Invalid JWT format. Expected 3 parts separated by dots, got: ${parts.size}"
    }

    val payload =
        parts[1].let {
            // Ensure the payload is base64 encoded correctly
            it.padEnd(it.length + (4 - it.length % 4) % 4, '=')
        }
    val decodedBytes = Base64.decode(payload)
    return try {
        json.decodeFromString<T>(decodedBytes.decodeToString())
    } catch (e: Exception) {
        throw IllegalArgumentException("Failed to decode JWT payload", e)
    }
}
