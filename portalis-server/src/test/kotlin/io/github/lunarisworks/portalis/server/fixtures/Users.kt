package io.github.lunarisworks.portalis.server.fixtures

import io.github.lunarisworks.portalis.server.features.users.domain.User
import io.github.lunarisworks.portalis.server.infrastructure.database.UserSchema
import io.github.lunarisworks.portalis.server.infrastructure.security.createPasswordEncoder
import org.jetbrains.exposed.v1.jdbc.insert
import kotlin.time.Clock
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

object Users {
    private val passwordEncoder = createPasswordEncoder()
    const val DEFAULT_PASSWORD = "StrongPassword123"

    val TestUser1 =
        User(
            id = Uuid.random(),
            username = "test_user1",
            email = "test.user1@test.com",
            name = "Test User 1",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
        )

    fun init() {
        UserSchema.insert {
            it[id] = TestUser1.id.toJavaUuid()
            it[username] = TestUser1.username
            it[email] = TestUser1.email
            it[name] = TestUser1.name
            it[passwordHash] = passwordEncoder.encode(DEFAULT_PASSWORD)
            it[createdAt] = TestUser1.createdAt
            it[updatedAt] = TestUser1.updatedAt
        }
    }
}
