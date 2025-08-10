package io.github.lunarisworks.portalis.server.features.journals

import io.github.lunarisworks.portalis.server.features.journals.data.JournalRepositoryImpl
import io.github.lunarisworks.portalis.server.features.journals.domain.JournalRepository
import io.github.lunarisworks.portalis.server.features.journals.domain.JournalService
import io.github.lunarisworks.portalis.server.features.journals.presentation.journalsRoutes
import io.github.lunarisworks.portalis.server.infrastructure.security.authenticate
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.routing.routing

fun Application.journalModule() {
    dependencies {
        provide<JournalRepository> { JournalRepositoryImpl() }
        provide<JournalService> { JournalService(resolve()) }
    }

    val journalService: JournalService by dependencies
    routing {
        authenticate {
            journalsRoutes(journalService)
        }
    }
}
