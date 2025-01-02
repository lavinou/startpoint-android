package com.lavinou.startpoint.auth.backend

import androidx.credentials.Credential
import com.lavinou.startpoint.auth.backend.model.SPAuthToken

/**
 * Interface representing the backend responsible for authenticating user credentials
 * and issuing authentication tokens.
 */
interface SPAuthenticationBackend {

    /**
     * The type of authentication supported by this backend (e.g., password, OAuth, etc.).
     */
    val type: String

    /**
     * Authenticates the provided credential and returns an authentication token.
     *
     * @param credential The credential to authenticate.
     * @return The resulting authentication token upon successful authentication.
     * @throws Exception if authentication fails.
     */
    suspend fun authenticate(credential: Credential): SPAuthToken
}