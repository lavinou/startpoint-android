package com.lavinou.startpoint.auth.biometric.presentation

import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.biometric.BiometricIdentifier
import com.lavinou.startpoint.auth.biometric.BiometricSPAuthBackend
import com.lavinou.startpoint.auth.biometric.core.BiometricSecurity
import com.lavinou.startpoint.auth.biometric.credentials.BiometricCredential
import com.lavinou.startpoint.auth.biometric.presentation.action.BiometricAction
import com.lavinou.startpoint.auth.biometric.presentation.effect.BiometricEffect
import com.lavinou.startpoint.auth.biometric.presentation.state.BiometricState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BiometricViewModel(
    private val backend: BiometricSPAuthBackend,
    private val security: BiometricSecurity
) : ViewModel() {

    private val _state = MutableStateFlow(BiometricState())
    val state: StateFlow<BiometricState>
        get() = _state

    private val _effect = MutableStateFlow<BiometricEffect?>(null)
    val effect: StateFlow<BiometricEffect?>
        get() = _effect

    fun dispatch(action: BiometricAction) {
        when (action) {

            is BiometricAction.Register -> {
                registerUserDevice(action.deviceId)
            }

            is BiometricAction.Authenticate -> {
                authenticateDevice(
                    result = action.result,
                    deviceId = action.deviceId,
                    auth = action.auth
                )
            }

            is BiometricAction.Reset -> {
                _effect.update {
                    null
                }

                _state.update {
                    BiometricState()
                }
            }
        }
    }

    private fun registerUserDevice(deviceId: String) {
        viewModelScope.launch {
            val key = BiometricSecurity.getPublicKey("test")
            key?.let {
                backend.register(
                    id = BiometricIdentifier(
                        device = deviceId
                    )
                )
            } ?: run {
                BiometricSecurity.generateKeyPair("test")
                val publicKey = BiometricSecurity.getPublicKey("test")
                backend.register(
                    id = BiometricIdentifier(
                        device = deviceId,
                        publicKey = publicKey
                    )
                )
            }
            _effect.update {
                BiometricEffect.RegistrationSuccess
            }
        }
    }

    private fun authenticateDevice(
        result: AuthenticationResult,
        deviceId: String,
        auth: SPAuth
    ) {
        viewModelScope.launch {
            val id = BiometricIdentifier(
                device = deviceId
            )
            val challenge = backend.challenge(
                id = id
            )
            val signedChallenge = BiometricSecurity.signChallenge(
                "test",
                challenge.toByteArray(),
                result.cryptoObject
            )
            signedChallenge?.let {
                val credential = BiometricCredential(
                    id = BiometricIdentifier(device = deviceId),
                    signedChallenge = it
                )
                val token = auth.authenticate(credential)
                _effect.update {
                    BiometricEffect.Success(token)
                }
            }
        }
    }
}