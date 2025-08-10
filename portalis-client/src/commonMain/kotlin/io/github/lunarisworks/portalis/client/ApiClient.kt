package io.github.lunarisworks.portalis.client

import io.github.lunarisworks.portalis.client.impl.ApiClientImpl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

interface ApiClient {
    val httpClient: HttpClient
    val auth: AuthApi
    val users: UserApi
    val journals: JournalApi

    val isLoggedIn: Boolean get() = currentUser() != null

    fun currentUser(): AuthUser?
}

fun ApiClient(
    baseUrl: String,
    authStorage: AuthStorage = AuthStorage(),
    block: HttpClientConfig<*>.() -> Unit = {},
): ApiClient = ApiClientImpl(baseUrl, authStorage, block)
