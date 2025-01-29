package com.lavinou.startpoint.auth.password

import androidx.compose.runtime.Composable
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

interface PasswordScreenProvider {

    @Composable
    fun LoginScreen(
        state: PasswordState,
        onBack: () -> Unit,
        onDispatch: (PasswordAction) -> Unit
    )

    @Composable
    fun RegistrationScreen()

    @Composable
    fun VerifyEmailScreen()

    @Composable
    fun ForgotPasswordScreen()

    @Composable
    fun ConfirmPasswordResetScreen()

}