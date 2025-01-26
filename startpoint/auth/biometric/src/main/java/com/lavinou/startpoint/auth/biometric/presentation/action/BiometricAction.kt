package com.lavinou.startpoint.auth.biometric.presentation.action

import androidx.biometric.BiometricPrompt.AuthenticationResult
import com.lavinou.startpoint.auth.SPAuth

internal sealed interface BiometricAction {

    data class Register(
        val result: AuthenticationResult,
        val deviceId: String
    ) : BiometricAction

    data class Authenticate(
        val result: AuthenticationResult,
        val deviceId: String,
        val auth: SPAuth
    ) : BiometricAction

    data object Reset : BiometricAction
}