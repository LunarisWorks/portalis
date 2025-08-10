package io.github.lunarisworks.portalis.server.integrations

import io.github.lunarisworks.portalis.server.fixtures.Users
import io.github.lunarisworks.portalis.server.infrastructure.PostgresExtension
import io.github.lunarisworks.portalis.server.infrastructure.authenticateTestUser1
import io.github.lunarisworks.portalis.server.infrastructure.testApplication
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.user.UserResponse
import io.kotest.assertions.ktor.client.shouldBeOK
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.http.HttpStatusCode

class UsersRouteTest : FunSpec() {
    init {
        install(PostgresExtension)

        context("Retrieve current authenticated user") {
            test("should return 200 when user is authenticated") {
                testApplication {
                    authenticateTestUser1()
                    val response = client.get(Routes.Users.Me())
                    response.shouldBeOK()
                    response.body<UserResponse>().username shouldBe Users.TestUser1.username
                }
            }

            test("should return 401 when user is not authenticated") {
                testApplication {
                    val response = client.get(Routes.Users.Me())

                    response shouldHaveStatus HttpStatusCode.Unauthorized
                }
            }
        }
    }
}
