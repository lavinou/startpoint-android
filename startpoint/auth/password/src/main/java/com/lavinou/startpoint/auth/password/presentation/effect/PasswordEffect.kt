package com.lavinou.startpoint.auth.password.presentation.effect

import com.lavinou.startpoint.auth.backend.model.SPAuthToken

internal sealed interface PasswordEffect {

    data class OnSuccess(
        val token: SPAuthToken
    ) : PasswordEffect
}