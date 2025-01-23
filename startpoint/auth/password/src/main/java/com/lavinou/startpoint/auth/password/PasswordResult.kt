package com.lavinou.startpoint.auth.password

import com.lavinou.startpoint.auth.backend.model.SPAuthToken

sealed interface PasswordResult {

    data class Success(
        val token: SPAuthToken,
        val registerForBiometrics: Boolean
    ) : PasswordResult

    data class BackendError(
        val throwable: Throwable
    ) : PasswordResult

    data class ValidationError(
        val key: String,
        val message: String
    ) : PasswordResult
}