package io.github.lunarisworks.portalis.server.features.auth.domain

import io.github.lunarisworks.portalis.server.core.DomainError
import io.github.lunarisworks.portalis.server.core.ServiceResult
import io.github.lunarisworks.portalis.server.core.asFailure
import io.github.lunarisworks.portalis.server.core.asSuccess
import io.github.lunarisworks.portalis.server.features.users.domain.User
import io.github.lunarisworks.portalis.server.infrastructure.database.dbQuery
import io.github.lunarisworks.portalis.server.infrastructure.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder

class AuthService(
    private val jwtService: JwtService,
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    suspend fun register(data: UserRegistration): ServiceResult<User> =
        dbQuery {
            if (authRepository.existsUser(data.username, data.email)) {
                return@dbQuery DomainError.UserAlreadyExists.asFailure()
            }

            val encodedPassword = passwordEncoder.encode(data.password)
            authRepository.registerUser(data.copy(password = encodedPassword)).asSuccess()
        }

    suspend fun login(data: LoginCredentials): ServiceResult<AuthenticateTokens> =
        dbQuery {
            val (user, passwordHash) =
                authRepository.findUserWithPassword(data.username)
                    ?: return@dbQuery DomainError.InvalidCredentials.asFailure()

            if (passwordHash == null) {
                return@dbQuery DomainError.PasswordNotSet.asFailure()
            }

            if (!(passwordEncoder.matches(data.password, passwordHash))) {
                return@dbQuery DomainError.InvalidCredentials.asFailure()
            }

            val refreshToken = createValidRefreshToken(user)

            AuthenticateTokens(
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = refreshToken,
            ).asSuccess()
        }

    private fun createValidRefreshToken(user: User): String {
        var attempts = 0
        var refreshToken: String

        do {
            if (attempts >= GENERATE_REFRESH_TOKEN_MAX_ATTEMPTS) {
                throw IllegalArgumentException("Invalid refresh token attempts")
            }

            refreshToken = jwtService.generateRefreshToken()
            attempts++
        } while (authRepository.isRefreshTokenExists(refreshToken))

        authRepository.insertRefreshToken(user.id, refreshToken)
        return refreshToken
    }

    companion object {
        const val GENERATE_REFRESH_TOKEN_MAX_ATTEMPTS = 5
    }
}
