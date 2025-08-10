package io.github.lunarisworks.portalis.server.infrastructure

import io.github.lunarisworks.portalis.server.fixtures.Users
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.TokensResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.server.testing.ApplicationTestBuilder

private var tokens: TokensResponse? = null

suspend fun ApplicationTestBuilder.authenticateTestUser1() {
    if (tokens == null) {
        tokens =
            client
                .post(Routes.Auth.Login()) {
                    setBody(
                        LoginRequest(
                            username = Users.TestUser1.username,
                            password = Users.DEFAULT_PASSWORD,
                        ),
                    )
                }.body()
    }
    client =
        client.config {
            defaultRequest {
                header(HttpHeaders.Authorization, "Bearer ${tokens!!.accessToken}")
            }
        }
}
