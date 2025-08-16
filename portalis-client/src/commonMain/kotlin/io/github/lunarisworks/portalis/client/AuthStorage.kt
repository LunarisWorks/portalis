package io.github.lunarisworks.portalis.client

import com.russhwolf.settings.Settings
import io.github.lunarisworks.portalis.client.impl.AuthStorageImpl
import kotlin.uuid.Uuid

data class AuthUser(
    val id: Uuid,
    val username: String,
    val email: String,
    val name: String? = null,
)

interface AuthStorage {
    fun getAccessToken(): String?

    fun setAccessToken(token: String?)

    fun setRefreshToken(token: String?)

    fun getRefreshToken(): String?

    fun clearTokens()
}

fun AuthStorage(
    settings: Settings = Settings(),
    storageKeyPrefix: String = AuthStorageImpl.DEFAULT_AUTH_STORAGE_KEY,
): AuthStorage = AuthStorageImpl(settings, storageKeyPrefix)
