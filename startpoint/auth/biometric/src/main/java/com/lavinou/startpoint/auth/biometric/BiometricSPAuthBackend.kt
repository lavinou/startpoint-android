package com.lavinou.startpoint.auth.biometric

import com.lavinou.startpoint.auth.backend.SPAuthenticationBackend
import com.lavinou.startpoint.auth.biometric.credentials.BiometricCredential

interface BiometricSPAuthBackend : SPAuthenticationBackend {

    override val type: String
        get() = BiometricCredential.TYPE

    suspend fun register(id: BiometricIdentifier): Boolean

    suspend fun unregister(id: BiometricIdentifier): Boolean

    suspend fun challenge(id: BiometricIdentifier): String

}