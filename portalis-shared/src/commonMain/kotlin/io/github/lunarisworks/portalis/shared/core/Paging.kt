package io.github.lunarisworks.portalis.shared.core

import io.github.lunarisworks.portalis.shared.core.CursorPageParams.Companion.DEFAULT_LIMIT
import kotlinx.serialization.Serializable

/**
 * Represents a paginated request using a cursor for pagination.
 * The [cursor] is typically a string that encodes the position in the dataset.
 */
interface CursorPageParams {
    /**
     * The cursor for pagination, which is a string that indicates the position in the dataset.
     * If null, it indicates the start of the dataset.
     */
    val cursor: String?

    /**
     * The maximum number of items to return in the response.
     * If null, a [default limit][DEFAULT_LIMIT] will be applied.
     */
    val limit: Int?

    companion object {
        /** Default limit for pagination requests. */
        const val DEFAULT_LIMIT: Int = 20

        /** Maximum limit for pagination requests. */
        const val MAX_LIMIT: Int = 100
    }
}

/**
 * Represents a paginated response using a cursor for pagination.
 * Contains a list of [items] and an optional (next cursor)[nextCursor] for further pagination.
 *
 * @param T The type of items in the response.
 */
@Serializable
data class CursorPageResponse<T>(
    val items: List<T>,
    val nextCursor: String? = null,
) {
    /** Checks if there is a next page available based on the presence of [nextCursor]. */
    operator fun hasNext(): Boolean = nextCursor != null

    /** Returns an iterator over the items in the response. */
    operator fun iterator(): Iterator<T> = items.iterator()

    /** Checks if the response contains no items. */
    fun isEmpty(): Boolean = items.isEmpty()

    /** Checks if the response contains items. */
    fun isNotEmpty(): Boolean = items.isNotEmpty()

    /** Returns the number of items in the response. */
    fun size(): Int = items.size
}
