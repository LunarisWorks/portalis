package io.github.lunarisworks.portalis.server.infrastructure

import io.github.lunarisworks.portalis.server.fixtures.Journals
import io.github.lunarisworks.portalis.server.fixtures.Users
import io.github.lunarisworks.portalis.shared.core.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.jsonIo
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.utils.io.KtorDsl
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import io.ktor.server.testing.testApplication as ktorTestApplication

private var testDataInitialized = false

private fun configureTestData() {
    if (testDataInitialized) {
        return
    }
    transaction {
        Users.init()
        Journals.init()
    }
    testDataInitialized = true
}

@OptIn(ExperimentalSerializationApi::class)
@KtorDsl
fun testApplication(
    envOverrides: (MutableMap<String, String>.() -> Unit)? = null,
    block: suspend ApplicationTestBuilder.() -> Unit,
) = ktorTestApplication {
    configure("application.yaml", "application-test.yaml") {
        this["postgres.host"] = PostgresExtension.container.host
        this["postgres.port"] = PostgresExtension.container.firstMappedPort.toString()
        this["postgres.database"] = PostgresExtension.container.databaseName
        this["postgres.user"] = PostgresExtension.container.username
        this["postgres.password"] = PostgresExtension.container.password
        envOverrides?.invoke(this)
    }

    application {
        configureTestData()
    }

    client =
        createClient {
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                jsonIo(Json)
            }
            install(Resources)
        }
    block()
}
