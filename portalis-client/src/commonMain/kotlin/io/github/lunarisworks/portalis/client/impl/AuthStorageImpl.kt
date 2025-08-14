package io.github.lunarisworks.portalis.client.impl

import com.russhwolf.settings.Settings
import io.github.lunarisworks.portalis.client.AuthStorage

internal class AuthStorageImpl(
    private val settings: Settings,
    private val storageKey: String,
) : AuthStorage {
    override fun getAccessToken(): String? = settings.getStringOrNull(storageKey)

    override fun setAccessToken(token: String?) {
        if (token == null) {
            settings.remove(storageKey)
        } else {
            settings.putString(storageKey, token)
        }
    }

    override fun clearTokens() {
        settings.remove(storageKey)
    }

    companion object {
        const val DEFAULT_AUTH_STORAGE_KEY = "portalis_auth_token"
    }
}
