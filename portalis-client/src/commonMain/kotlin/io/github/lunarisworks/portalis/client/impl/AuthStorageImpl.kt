package io.github.lunarisworks.portalis.client.impl

import com.russhwolf.settings.Settings
import io.github.lunarisworks.portalis.client.AuthStorage

internal class AuthStorageImpl(
    private val settings: Settings,
) : AuthStorage {
    override fun getAccessToken(): String? = settings.getStringOrNull(AUTH_TOKEN_KEY)

    override fun setAccessToken(token: String?) {
        if (token == null) {
            settings.remove(AUTH_TOKEN_KEY)
        } else {
            settings.putString(AUTH_TOKEN_KEY, token)
        }
    }

    override fun clearTokens() {
        settings.remove(AUTH_TOKEN_KEY)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "portalis_auth_token"
    }
}
