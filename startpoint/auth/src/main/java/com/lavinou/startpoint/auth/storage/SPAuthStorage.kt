package com.lavinou.startpoint.auth.storage

import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import kotlinx.coroutines.flow.Flow

/**
 * Interface for handling the storage and retrieval of authentication tokens in SPAuth.
 * Implementations of this interface manage persisting and observing token changes.
 */
interface SPAuthStorage {

    /**
     * The current authentication token, if available.
     */
    val token: SPAuthToken?

    /**
     * A flow that emits updates whenever the authentication token changes.
     */
    val tokenFlow: Flow<SPAuthToken?>

    /**
     * Persists the provided authentication token.
     *
     * @param token The authentication token to save. Pass null to clear the token.
     */
    fun save(token: SPAuthToken?)

    /**
     * Retrieves the currently stored authentication token.
     *
     * @return The retrieved authentication token.
     */
    fun retrieve(): SPAuthToken
}