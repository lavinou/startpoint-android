package com.lavinou.startpoint.auth.backend

import com.lavinou.startpoint.auth.SPAuthUser
import com.lavinou.startpoint.auth.backend.model.SPAuthToken

/**
 * Interface representing the backend responsible for managing user sessions.
 * This includes handling user retrieval and logout operations.
 *
 * @param TUser The type of authenticated user managed by this session backend.
 */
interface SPUserSessionBackend<out TUser : SPAuthUser<*>> {

    /**
     * Logs out the user associated with the given authentication token.
     *
     * @param token The authentication token representing the user's session.
     * @return True if logout was successful, false otherwise.
     */
    suspend fun logout(token: SPAuthToken): Boolean

    /**
     * Retrieves the user associated with the provided authentication token.
     *
     * @param token The authentication token used to identify the user. Pass null if no token is available.
     * @return The authenticated user associated with the token.
     * @throws Exception if user retrieval fails.
     */
    suspend fun user(token: SPAuthToken?): TUser
}