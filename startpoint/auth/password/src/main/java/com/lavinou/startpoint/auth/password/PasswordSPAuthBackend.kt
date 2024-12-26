package com.lavinou.startpoint.auth.password

import androidx.credentials.PasswordCredential
import com.lavinou.startpoint.auth.backend.SPAuthenticationBackend

interface PasswordSPAuthBackend : SPAuthenticationBackend {

    override val type: String
        get() = PasswordCredential.TYPE_PASSWORD_CREDENTIAL

    suspend fun resetPassword(email: String): Boolean

    suspend fun confirmPasswordReset(): Boolean

    suspend fun changePassword(): Boolean
}