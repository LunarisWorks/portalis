package io.github.lunarisworks.portalis.shared.core

import io.github.lunarisworks.portalis.shared.core.errors.ApiError
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.json.Json as KotlinxJson

/**
 * A [Json] instance configured for the Portalis application.
 *
 * This instance is used in the server and client to serialize and deserialize models and API responses.
 */
@OptIn(ExperimentalSerializationApi::class)
val Json =
    KotlinxJson {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        decodeEnumsCaseInsensitive = true
        namingStrategy = JsonNamingStrategy.SnakeCase
        serializersModule =
            SerializersModule {
                polymorphic(ApiError::class) {
                    subclass(ApiError.ResourceNotFound::class)
                    subclass(ApiError.ResourceAlreadyExists::class)
                    subclass(ApiError.InvalidCredentials::class)
                    subclass(ApiError.PasswordNotSet::class)
                    subclass(ApiError.ValidationError::class)
                    defaultDeserializer { ApiError.UndefinedError.serializer() }
                }
            }
    }
