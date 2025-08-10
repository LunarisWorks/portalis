package io.github.lunarisworks.portalis.server.features.users.domain

import io.github.lunarisworks.portalis.server.core.DomainError
import io.github.lunarisworks.portalis.server.core.ServiceResult
import io.github.lunarisworks.portalis.server.core.asFailure
import io.github.lunarisworks.portalis.server.core.asSuccess
import io.github.lunarisworks.portalis.server.infrastructure.database.dbQuery
import kotlin.uuid.Uuid

class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun get(id: Uuid): ServiceResult<User> =
        dbQuery {
            userRepository.find(id)?.asSuccess() ?: DomainError.UserNotFound(id).asFailure()
        }
}
