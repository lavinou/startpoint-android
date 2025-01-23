package com.lavinou.startpoint.auth.password.presentation.effect

import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import java.lang.Exception

internal sealed interface PasswordEffect {

    data class OnSuccess(
        val token: SPAuthToken
    ) : PasswordEffect

    data class OnError(
        val exception: Exception
    ) :  PasswordEffect
}