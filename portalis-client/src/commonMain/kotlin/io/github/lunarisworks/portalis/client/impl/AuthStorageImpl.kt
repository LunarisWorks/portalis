package io.github.lunarisworks.portalis.client.impl

import com.russhwolf.settings.Settings
import io.github.lunarisworks.portalis.client.AuthStorage

internal class AuthStorageImpl(
    private val settings: Settings,
    private val storageKeyPrefix: String,
) : AuthStorage {
    val accessTokenKey = "$storageKeyPrefix/$ACCESS_TOKEN_KEY"
    val refreshTokenKey = "$storageKeyPrefix/$REFRESH_TOKEN_KEY"

    override fun getAccessToken(): String? = settings.getStringOrNull(accessTokenKey)

    override fun setAccessToken(token: String?) {
        if (token == null) {
            settings.remove(accessTokenKey)
        } else {
            settings.putString(accessTokenKey, token)
        }
    }

    override fun setRefreshToken(token: String?) {
        if (token == null) {
            settings.remove(refreshTokenKey)
        } else {
            settings.putString(refreshTokenKey, token)
        }
    }

    override fun getRefreshToken(): String? = settings.getStringOrNull(refreshTokenKey)

    override fun clearTokens() {
        settings.remove(accessTokenKey)
        settings.remove(refreshTokenKey)
    }

    companion object {
        const val DEFAULT_AUTH_STORAGE_KEY = "portalis_auth"
        private val ACCESS_TOKEN_KEY = "access_token"
        private val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
