package com.lavinou.startpoint.auth.backend.model

/**
 * Represents an authentication token containing access and refresh tokens,
 * as well as the expiration time.
 */
interface SPAuthToken {

    /**
     * The token used to access protected resources.
     */
    val accessToken: String

    /**
     * The token used to refresh the access token when it expires.
     */
    val refreshToken: String

    /**
     * The expiration time of the access token, represented in milliseconds since epoch.
     */
    val expiresAt: Long

    companion object {
        /**
         * A default instance of SPAuthToken with empty tokens and an expired state.
         */
        val Default: SPAuthToken = object : SPAuthToken {
            override val accessToken: String
                get() = ""

            override val refreshToken: String
                get() = ""

            override val expiresAt: Long
                get() = -1
        }
    }
}