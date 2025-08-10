package io.github.lunarisworks.portalis.shared.core.errors

import kotlinx.serialization.Serializable

@Serializable
data class FieldError(
    val path: String,
    val message: String,
)
