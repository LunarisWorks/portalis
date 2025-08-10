package io.github.lunarisworks.portalis.server.core

import io.github.lunarisworks.portalis.shared.core.CursorPageResponse

data class CursorPaged<T>(
    val items: List<T>,
    val nextCursor: String? = null,
)

fun <T, R> CursorPaged<T>.toResponse(transform: (T) -> R) =
    CursorPageResponse(
        items = items.map(transform),
        nextCursor = nextCursor,
    )
