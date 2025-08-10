package io.github.lunarisworks.portalis.client.impl

import io.github.lunarisworks.portalis.client.ApiClient
import io.github.lunarisworks.portalis.client.AuthApi
import io.github.lunarisworks.portalis.client.AuthStorage
import io.github.lunarisworks.portalis.client.AuthUser
import io.github.lunarisworks.portalis.client.JournalApi
import io.github.lunarisworks.portalis.client.UserApi
import io.github.lunarisworks.portalis.shared.core.Json
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.jsonIo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

internal class ApiClientImpl(
    private val baseUrl: String,
    private val authStorage: AuthStorage,
    config: HttpClientConfig<*>.() -> Unit,
) : ApiClient {
    init {
        retrieveCurrentUser()
    }

    @OptIn(ExperimentalSerializationApi::class)
    override val httpClient: HttpClient =
        HttpClient {
            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
            }
            install(Resources)
            install(ContentNegotiation) {
                jsonIo(Json)
            }
            install(Logging)
            install(Auth) {
                bearer {
                    loadTokens {
                        authStorage.getAccessToken()?.let {
                            BearerTokens(it, null)
                        }
                    }
                    sendWithoutRequest {
                        it.url.encodedPath.startsWith(NON_AUTHORIZED_PATH)
                    }
                }
            }
            config()
        }
    var currentUser: AuthUser? = null

    private fun retrieveCurrentUser() {
        val token = authStorage.getAccessToken() ?: return
        currentUser =
            decodeJwt<JwtPayload>(token).let {
                AuthUser(
                    id = it.sub,
                    username = it.username,
                    email = it.email,
                    name = it.name,
                )
            }
    }

    override val auth: AuthApi =
        AuthApiImpl(
            httpClient,
            onLoginSuccess = {
                authStorage.setAccessToken(it.accessToken)
                retrieveCurrentUser()
            },
            onLogoutSuccess = {
                authStorage.clearTokens()
                currentUser = null
            },
        )
    override val users: UserApi = UserApiImpl(httpClient)
    override val journals: JournalApi = JournalApiImpl(httpClient)

    override fun currentUser(): AuthUser? = currentUser

    companion object {
        private const val NON_AUTHORIZED_PATH = "auth"
    }
}

@Serializable
private data class JwtPayload(
    val sub: Uuid,
    val username: String,
    val email: String,
    val name: String?,
)
