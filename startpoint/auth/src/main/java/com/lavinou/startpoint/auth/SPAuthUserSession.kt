package com.lavinou.startpoint.auth

import com.lavinou.startpoint.auth.backend.SPUserSessionBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.storage.SPAuthStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Manages the user session for authenticated users, providing access to user information
 * and handling session lifecycle events such as login and logout.
 *
 * @param TUser The type of authenticated user managed by this session.
 * @param storage The storage mechanism for persisting authentication tokens.
 * @param backend The backend responsible for handling user session operations.
 * @param dispatcher The coroutine dispatcher used for background operations.
 */
class SPAuthUserSession<out TUser : SPAuthUser<*>>(
    private val storage: SPAuthStorage,
    private val backend: SPUserSessionBackend<TUser>,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var currentToken: SPAuthToken? = null
    private var currentUser: TUser? = null
    private val scope = CoroutineScope(dispatcher)

    init {
        currentToken = storage.retrieve()
        scope.launch {
            currentUser = user(currentToken)
        }
    }

    /**
     * The currently authenticated user, if available.
     */
    public val user: TUser?
        get() = currentUser

    /**
     * A flow that emits updates whenever the authenticated user changes.
     */
    public val userFlow: Flow<TUser>
        get() = storage.tokenFlow
            .distinctUntilChanged()
            .map {
                updateUser(it)
            }

    init {
        currentToken = storage.retrieve()
    }

    /**
     * Logs the user out by invalidating the current session and clearing the stored token.
     *
     * @return True if logout was successful, false otherwise.
     */
    public suspend fun logout(): Boolean {
        currentToken?.let { token ->
            backend.logout(token)
        }
        currentToken = null
        storage.save(null)
        return true
    }

    /**
     * Checks if the user is currently logged in by validating the presence and expiration of the access token.
     *
     * @return True if the user is logged in and the token is valid, false otherwise.
     */
    public fun isLoggedIn(): Boolean {
        return currentToken != null && currentToken?.accessToken != null
                && currentToken!!.expiresAt > System.currentTimeMillis()
    }

    private suspend fun user(token: SPAuthToken?): TUser {
        return backend.user(token)
    }

    private suspend fun updateUser(token: SPAuthToken?): TUser {
        val newUser = user(token)
        currentUser = newUser
        return newUser
    }

}