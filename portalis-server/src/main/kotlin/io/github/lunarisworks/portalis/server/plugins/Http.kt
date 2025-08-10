package io.github.lunarisworks.portalis.server.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse

fun Application.configureHttp() {
    install(AutoHeadResponse)
}
