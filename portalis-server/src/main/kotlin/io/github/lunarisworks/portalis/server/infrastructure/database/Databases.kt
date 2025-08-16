package io.github.lunarisworks.portalis.server.infrastructure.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Suppress("DEPRECATION")
internal suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

internal fun connectToPostgres(config: DatabaseConfig): Database {
    val url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}"
    val ds =
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = url
                driverClassName = "org.postgresql.Driver"
                username = config.username
                password = config.password
                maximumPoolSize = 6
                // as of version 0.46.0, if these options are set here, they do not need to be duplicated in DatabaseConfig
                isReadOnly = false
                transactionIsolation = "TRANSACTION_SERIALIZABLE"
            },
        )

    return Database.connect(ds)
}

internal fun migrateDatabase(database: Database) {
    transaction(database) {
        SchemaUtils.create(UserSchema, JournalSchema, RefreshTokenSchema)
    }
}
