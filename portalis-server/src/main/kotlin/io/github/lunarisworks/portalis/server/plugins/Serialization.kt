package io.github.lunarisworks.portalis.server.plugins

import io.github.lunarisworks.portalis.shared.core.Json
import io.ktor.serialization.kotlinx.json.jsonIo
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureSerialization() {
    install(Resources)
    install(ContentNegotiation) {
        jsonIo(Json)
    }
}
