package io.github.lunarisworks.portalis.shared

import io.github.lunarisworks.portalis.shared.auth.AuthRoutes
import io.github.lunarisworks.portalis.shared.core.CursorPageParams
import io.github.lunarisworks.portalis.shared.journal.JournalRoutes
import io.github.lunarisworks.portalis.shared.user.UserRoutes
import io.ktor.resources.Resource
import kotlin.uuid.Uuid

/** Type-safe representation of the API routes. */
object Routes {
    /** Base path for authentication-related routes. */
    @Resource(AuthRoutes.BASE_PATH)
    data object Auth {
        /** Route for user registration. */
        @Resource(AuthRoutes.REGISTER_PATH)
        data class Register(
            val parent: Auth = Auth,
        )

        /** Route for user login. */
        @Resource(AuthRoutes.LOGIN_PATH)
        data class Login(
            val parent: Auth = Auth,
        )

        /** Route for refreshing authentication tokens. */
        @Resource(AuthRoutes.REFRESH_PATH)
        data class Refresh(
            val parent: Auth = Auth,
        )
    }

    /** Base path for user-related routes. */
    @Resource(UserRoutes.BASE_PATH)
    data object Users {
        /** Route to access the authenticated user's information. */
        @Resource(UserRoutes.ME_PATH)
        data class Me(
            val parent: Users = Users,
        )
    }

    /** Base path for journal-related routes. */
    @Resource(JournalRoutes.BASE_PATH)
    data object Journals {
        /** Route to list journals with pagination support. */
        @Resource("")
        data class List(
            val parent: Journals = Journals,
            override val cursor: String? = null,
            override val limit: Int = CursorPageParams.DEFAULT_LIMIT,
        ) : CursorPageParams

        /** Route to get a specific journal by its ID. */
        @Resource(JournalRoutes.ID_PATH)
        data class Get(
            val parent: Journals = Journals,
            val id: Uuid,
        )

        /** Route to create a new journal. */
        @Resource("")
        data class Create(
            val parent: Journals = Journals,
        )

        /** Route to update an existing journal by its ID. */
        @Resource(JournalRoutes.ID_PATH)
        data class Patch(
            val parent: Journals = Journals,
            val id: Uuid,
        )

        /** Route to delete a specific journal by its ID. */
        @Resource(JournalRoutes.ID_PATH)
        data class Delete(
            val parent: Journals = Journals,
            val id: Uuid,
        )
    }
}
