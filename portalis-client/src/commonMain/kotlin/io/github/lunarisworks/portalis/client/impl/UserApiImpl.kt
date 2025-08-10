package io.github.lunarisworks.portalis.client.impl

import io.github.lunarisworks.portalis.client.UserApi
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.user.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get

internal class UserApiImpl(
    private val httpClient: HttpClient,
) : UserApi {
    override suspend fun me(): ApiResult<UserResponse> =
        httpClient
            .get(Routes.Users.Me())
            .toApiResult()
}
