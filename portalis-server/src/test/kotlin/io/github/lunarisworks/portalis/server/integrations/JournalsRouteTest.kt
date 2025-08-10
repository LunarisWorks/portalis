package io.github.lunarisworks.portalis.server.integrations

import io.github.lunarisworks.portalis.server.fixtures.Journals.TestJournal1
import io.github.lunarisworks.portalis.server.fixtures.giveMeOne
import io.github.lunarisworks.portalis.server.infrastructure.PostgresExtension
import io.github.lunarisworks.portalis.server.infrastructure.authenticateTestUser1
import io.github.lunarisworks.portalis.server.infrastructure.testApplication
import io.github.lunarisworks.portalis.shared.Routes
import io.github.lunarisworks.portalis.shared.core.CursorPageResponse
import io.github.lunarisworks.portalis.shared.journal.CreateJournalRequest
import io.github.lunarisworks.portalis.shared.journal.JournalResponse
import io.github.lunarisworks.portalis.shared.journal.PatchJournalRequest
import io.kotest.assertions.ktor.client.shouldHaveHeader
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.patch
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlin.uuid.Uuid

class JournalsRouteTest : FunSpec() {
    init {
        install(PostgresExtension)

        context("Create journal entry") {
            test("should return 201 when creating a new journal entry") {
                testApplication {
                    authenticateTestUser1()
                    val response =
                        client.post(Routes.Journals.Create()) {
                            setBody(CreateJournalRequest::class.giveMeOne())
                        }

                    response.shouldHaveStatus(HttpStatusCode.Created)
                    response.shouldHaveHeader(HttpHeaders.Location)
                    val body = shouldNotThrowAny { response.body<JournalResponse>() }
                    body.id shouldNotBe Uuid.NIL
                }
            }
        }

        context("Get journal entry") {
            test("should return 200 when fetching an existing journal entry") {
                testApplication {
                    authenticateTestUser1()
                    val response = client.get(Routes.Journals.Get(id = TestJournal1.id))
                    response.shouldHaveStatus(HttpStatusCode.OK)
                    val body = shouldNotThrowAny { response.body<JournalResponse>() }
                    body.id shouldBe TestJournal1.id
                }
            }

            test("should return 404 when fetching a non-existing journal entry") {
                testApplication {
                    authenticateTestUser1()
                    val response = client.get(Routes.Journals.Get(id = Uuid.fromLongs(Long.MAX_VALUE, Long.MAX_VALUE)))
                    response shouldHaveStatus HttpStatusCode.NotFound
                }
            }
        }
        context("List journal entries") {
            test("should return 200 when listing journal entries") {
                testApplication {
                    authenticateTestUser1()
                    val response = client.get(Routes.Journals.List())

                    response.shouldHaveStatus(HttpStatusCode.OK)
                    val body = shouldNotThrowAny { response.body<CursorPageResponse<JournalResponse>>() }
                    body.items.shouldNotBeEmpty()
                }
            }
            test("should return 200 when listing journal entries with pagination") {
                testApplication {
                    authenticateTestUser1()
                    val response = client.get(Routes.Journals.List(limit = 3))

                    response.shouldHaveStatus(HttpStatusCode.OK)
                    val body = shouldNotThrowAny { response.body<CursorPageResponse<JournalResponse>>() }
                    body.items.shouldNotBeEmpty()
                    body.items.size shouldBe 3
                    body.nextCursor shouldNotBe null

                    val nextResponse = client.get(Routes.Journals.List(cursor = body.nextCursor, limit = 3))
                    nextResponse.shouldHaveStatus(HttpStatusCode.OK)
                    val nextBody = shouldNotThrowAny { nextResponse.body<CursorPageResponse<JournalResponse>>() }
                    nextBody.items.shouldNotBeEmpty()
                }
            }
        }
        context("Patch journal entry") {
            test("should return 200 when updating an existing journal entry") {
                testApplication {
                    authenticateTestUser1()
                    val journalPatch =
                        PatchJournalRequest(
                            title = String::class.giveMeOne(),
                        )
                    val response =
                        client.patch(Routes.Journals.Patch(id = TestJournal1.id)) {
                            setBody(journalPatch)
                        }

                    response.shouldHaveStatus(HttpStatusCode.OK)
                    val body = shouldNotThrowAny { response.body<JournalResponse>() }
                    body.id shouldBe TestJournal1.id
                    body.title shouldBe journalPatch.title
                }
            }

            test("should return 404 when updating a non-existing journal entry") {
                testApplication {
                    authenticateTestUser1()
                    val response =
                        client.post(Routes.Journals.Patch(id = Uuid.fromLongs(Long.MAX_VALUE, Long.MAX_VALUE))) {
                            setBody(CreateJournalRequest::class.giveMeOne())
                        }

                    response shouldHaveStatus HttpStatusCode.NotFound
                }
            }
        }
    }
}
