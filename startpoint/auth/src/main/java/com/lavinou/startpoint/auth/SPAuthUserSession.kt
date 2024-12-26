package com.lavinou.startpoint.auth

import com.lavinou.startpoint.auth.backend.SPUserSessionBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.storage.SPAuthStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

    val user: TUser?
        get() = currentUser

    val userFlow: Flow<TUser>
        get() = storage.tokenFlow.map {
            user(it)
        }

    init {
        currentToken = storage.retrieve()
    }

    suspend fun logout(): Boolean {
        currentToken?.let { token ->
            backend.logout(token)
        }
        currentToken = null
        storage.save(null)
        return true
    }

    fun isLoggedIn(): Boolean {
        return currentToken != null && currentToken?.accessToken != null
                && currentToken!!.expiresAt > System.currentTimeMillis()
    }

    suspend fun user(token: SPAuthToken?): TUser {
        return backend.user(token)
    }


}