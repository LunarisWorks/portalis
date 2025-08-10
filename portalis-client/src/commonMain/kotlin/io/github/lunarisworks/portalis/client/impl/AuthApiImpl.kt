package io.github.lunarisworks.portalis.client.impl

import io.github.lunarisworks.portalis.client.AuthApi
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.github.lunarisworks.portalis.shared.auth.TokensResponse
import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.core.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

internal class AuthApiImpl(
    private val httpClient: HttpClient,
    private val onLoginSuccess: suspend (TokensResponse) -> Unit = {},
    private val onLogoutSuccess: suspend () -> Unit = {},
) : AuthApi {
    override suspend fun register(request: RegisterRequest): ApiResult<Unit> =
        httpClient
            .post(Routes.Auth.Register()) {
                setBody(request)
            }.toApiResult()

    override suspend fun login(request: LoginRequest): ApiResult<TokensResponse> =
        httpClient
            .post(Routes.Auth.Login()) {
                setBody(request)
            }.toApiResult<TokensResponse>()
            .onSuccess { onLoginSuccess(it) }

    override suspend fun logout() {
        onLogoutSuccess()
    }
}
