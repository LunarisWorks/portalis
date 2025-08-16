package io.github.lunarisworks.portalis.server.integrations

import io.github.lunarisworks.portalis.server.fixtures.Users
import io.github.lunarisworks.portalis.server.infrastructure.PostgresExtension
import io.github.lunarisworks.portalis.server.infrastructure.testApplication
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.auth.LoginRequest
import io.github.lunarisworks.portalis.shared.auth.RefreshTokenRequest
import io.github.lunarisworks.portalis.shared.auth.RegisterRequest
import io.github.lunarisworks.portalis.shared.auth.TokensResponse
import io.kotest.assertions.ktor.client.shouldBeOK
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class AuthRouteTest : FunSpec() {
    init {
        install(PostgresExtension)

        context("Register new user") {
            test("should return 201 when register new user successful") {
                testApplication {
                    val response =
                        client.post(Routes.Auth.Register()) {
                            setBody(
                                RegisterRequest(
                                    username = "new_user",
                                    email = "new.user@test.com",
                                    password = "StrongPassword123",
                                    name = "New User",
                                ),
                            )
                        }

                    response.shouldHaveStatus(HttpStatusCode.Created)
                }
            }
            test("should return 409 when user with same email or username already exists") {
                testApplication {
                    val response =
                        client.post(Routes.Auth.Register()) {
                            setBody(
                                RegisterRequest(
                                    username = "new_user",
                                    email = Users.TestUser1.email,
                                    password = "AnotherStrongPassword123",
                                    name = "New User",
                                ),
                            )
                        }

                    response.shouldHaveStatus(HttpStatusCode.Conflict)
                }
            }
        }

        context("Login") {
            test("should return 200 when login with correct credentials") {
                testApplication {
                    val response =
                        client.post(Routes.Auth.Login()) {
                            setBody(
                                LoginRequest(
                                    username = Users.TestUser1.username,
                                    password = Users.DEFAULT_PASSWORD,
                                ),
                            )
                        }

                    response.shouldBeOK()
                    response.body<TokensResponse>().accessToken.shouldNotBeBlank()
                    response.body<TokensResponse>().refreshToken.shouldNotBeBlank()
                }
            }
            test("should return 401 when login with incorrect credentials") {
                testApplication {
                    val response =
                        client.post(Routes.Auth.Login()) {
                            setBody(
                                LoginRequest(
                                    username = Users.TestUser1.username,
                                    password = "WrongPassword123",
                                ),
                            )
                        }

                    response.shouldHaveStatus(HttpStatusCode.Unauthorized)
                }
            }
        }

        context("Refresh Token") {
            test("should return 200 when refresh token is valid") {
                testApplication {
                    val loginResponse =
                        client.post(Routes.Auth.Login()) {
                            setBody(
                                LoginRequest(
                                    username = Users.TestUser1.username,
                                    password = Users.DEFAULT_PASSWORD,
                                ),
                            )
                        }

                    val tokens = loginResponse.body<TokensResponse>()
                    val response =
                        client.post(Routes.Auth.Refresh()) {
                            setBody(RefreshTokenRequest(tokens.refreshToken))
                        }

                    response.shouldBeOK()
                    val newTokens = response.body<TokensResponse>()
                    newTokens.accessToken.shouldNotBeBlank()
                    newTokens.refreshToken.shouldNotBeBlank()
                    newTokens.refreshToken shouldNotBe tokens.refreshToken
                }
            }
            test("should return 401 when refresh token is invalid") {
                testApplication {
                    val response =
                        client.post(Routes.Auth.Refresh()) {
                            setBody(RefreshTokenRequest("InvalidToken123"))
                        }

                    response.shouldHaveStatus(HttpStatusCode.Unauthorized)
                }
            }
            test("should return 401 when refresh token is expired") {
                testApplication(
                    {
                        this["security.jwt.refresh-token.expires-in"] = "1ms"
                    },
                ) {
                    val loginResponse =
                        client.post(Routes.Auth.Login()) {
                            setBody(
                                LoginRequest(
                                    username = Users.TestUser1.username,
                                    password = Users.DEFAULT_PASSWORD,
                                ),
                            )
                        }

                    val tokens = loginResponse.body<TokensResponse>()
                    val response =
                        client.post(Routes.Auth.Refresh()) {
                            setBody(RefreshTokenRequest(tokens.refreshToken))
                        }

                    response.shouldHaveStatus(HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}
