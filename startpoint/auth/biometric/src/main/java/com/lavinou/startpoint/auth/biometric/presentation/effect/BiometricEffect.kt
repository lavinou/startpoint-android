package com.lavinou.startpoint.auth.biometric.presentation.effect

import com.lavinou.startpoint.auth.backend.model.SPAuthToken

internal sealed interface BiometricEffect {

    data object RegistrationSuccess : BiometricEffect

    data class Success(
        val token: SPAuthToken
    ) : BiometricEffect
}