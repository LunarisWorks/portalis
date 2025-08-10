package io.github.lunarisworks.portalis.server.infrastructure

import io.kotest.core.extensions.MountableExtension
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.TestListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

object PostgresExtension :
    MountableExtension<GenericContainer<*>, Unit>,
    AfterProjectListener,
    TestListener {
    val container =
        PostgreSQLContainer("postgres:17-alpine")
            .apply {
                withDatabaseName("postgres")
                withUsername("postgres")
                withPassword("postgres")
            }

    override fun mount(configure: GenericContainer<*>.() -> Unit) {
        configure(container)
        container.start()
    }

    override suspend fun afterProject() {
        withContext(Dispatchers.IO) {
            container.stop()
        }
    }
}
