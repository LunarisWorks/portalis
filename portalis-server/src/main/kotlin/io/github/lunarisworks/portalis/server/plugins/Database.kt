package io.github.lunarisworks.portalis.server.plugins

import io.github.lunarisworks.portalis.server.infrastructure.database.connectToPostgres
import io.github.lunarisworks.portalis.server.infrastructure.database.migrateDatabase
import io.ktor.server.application.Application
import io.ktor.server.config.property
import io.ktor.server.plugins.di.dependencies
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabase() {
    dependencies {
        provide<Database> { connectToPostgres(this@configureDatabase.property("postgres")) }
    }

    val database: Database by dependencies
    migrateDatabase(database)
}
