package io.github.lunarisworks.portalis.server.features.users.domain

import kotlin.uuid.Uuid

interface UserRepository {
    fun find(id: Uuid): User?
}
