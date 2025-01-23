package com.lavinou.startpoint.auth.biometric

import com.lavinou.startpoint.auth.backend.model.SPAuthToken

sealed interface BiometricResult {

    data class Success(
        val token: SPAuthToken
    ) : BiometricResult

    data class Failure(
        val throwable: Throwable
    ) : BiometricResult

    data object RegistrationSuccess: BiometricResult
}