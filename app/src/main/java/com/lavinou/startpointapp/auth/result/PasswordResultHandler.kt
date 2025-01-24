package com.lavinou.startpointapp.auth.result

import com.lavinou.startpoint.auth.biometric.navigation.BiometricRegistration
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.password.PasswordConfiguration
import com.lavinou.startpoint.auth.password.PasswordResult
import com.lavinou.startpoint.navigation.MainContent

fun PasswordConfiguration.resultHandlers() {

    onResult = { result ->
        when (result) {
            is PasswordResult.Success -> {
                if (result.registerForBiometrics.not())
                    SPAuthNextAction.NavigateTo(
                        route = BiometricRegistration
                    )
                else
                    SPAuthNextAction.NavigateTo(
                        route = MainContent
                    )
            }

            is PasswordResult.ValidationError -> {
                SPAuthNextAction.FieldMessage(
                    field = result.key,
                    message = result.message
                )
            }

            is PasswordResult.BackendError -> {
                SPAuthNextAction.FieldMessage(
                    field = "server",
                    message = "Email or password is incorrect"
                )
            }

        }
    }
}