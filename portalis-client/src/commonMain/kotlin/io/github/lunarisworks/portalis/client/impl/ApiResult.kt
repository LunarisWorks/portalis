package io.github.lunarisworks.portalis.client.impl

import io.github.lunarisworks.portalis.shared.core.ApiResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpResponse.toApiResult(): ApiResult<T> =
    if (status.isSuccess()) {
        ApiResult.Success(value = body())
    } else {
        ApiResult.Failure(error = body())
    }
