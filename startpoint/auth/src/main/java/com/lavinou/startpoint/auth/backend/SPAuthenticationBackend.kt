package com.lavinou.startpoint.auth.backend

import androidx.credentials.Credential
import com.lavinou.startpoint.auth.backend.model.SPAuthToken

interface SPAuthenticationBackend {

    val type: String

    suspend fun authenticate(credential: Credential): SPAuthToken

}