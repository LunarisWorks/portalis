package io.github.lunarisworks.portalis.server

import io.github.lunarisworks.portalis.server.features.auth.authModule
import io.github.lunarisworks.portalis.server.features.journals.journalModule
import io.github.lunarisworks.portalis.server.features.users.userModule
import io.github.lunarisworks.portalis.server.plugins.configureDatabase
import io.github.lunarisworks.portalis.server.plugins.configureExceptionHandling
import io.github.lunarisworks.portalis.server.plugins.configureHttp
import io.github.lunarisworks.portalis.server.plugins.configureMonitoring
import io.github.lunarisworks.portalis.server.plugins.configureRequestValidation
import io.github.lunarisworks.portalis.server.plugins.configureSecurity
import io.github.lunarisworks.portalis.server.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureHttp()
    configureExceptionHandling()
    configureMonitoring()
    configureRequestValidation()

    configureSecurity()

    userModule()
    authModule()
    journalModule()
}
