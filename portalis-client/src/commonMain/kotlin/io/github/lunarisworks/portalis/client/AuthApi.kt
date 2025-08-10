package io.github.lunarisworks.portalis.client

import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.github.lunarisworks.portalis.shared.auth.TokensResponse
import io.github.lunarisworks.portalis.shared.core.ApiResult

interface AuthApi {
    suspend fun register(request: RegisterRequest): ApiResult<Unit>

    suspend fun login(request: LoginRequest): ApiResult<TokensResponse>

    suspend fun logout()
}
