package io.github.lunarisworks.portalis.client

import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.github.lunarisworks.portalis.shared.user.UserResponse

interface UserApi {
    suspend fun me(): ApiResult<UserResponse>
}
