package com.lavinou.startpoint.auth.backend

import com.lavinou.startpoint.auth.SPAuthUser
import com.lavinou.startpoint.auth.backend.model.SPAuthToken

interface SPUserSessionBackend<out TUser : SPAuthUser<*>> {

    suspend fun logout(token: SPAuthToken): Boolean

    suspend fun user(token: SPAuthToken?): TUser
}