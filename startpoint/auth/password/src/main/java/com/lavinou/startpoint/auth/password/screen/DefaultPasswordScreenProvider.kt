package com.lavinou.startpoint.auth.password.screen

import androidx.compose.runtime.Composable
import com.lavinou.startpoint.auth.password.PasswordScreenProvider
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.component.DefaultLoginContent
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

internal class DefaultPasswordScreenProvider : PasswordScreenProvider {

    @Composable
    override fun LoginScreen(
        state: PasswordState,
        onBack: () -> Unit,
        onDispatch: (PasswordAction) -> Unit
    ) {

        DefaultLoginContent(state = state, onBack = onBack, onDispatch = onDispatch)
    }

    @Composable
    override fun RegistrationScreen() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun VerifyEmailScreen() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun ForgotPasswordScreen() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun ConfirmPasswordResetScreen() {
        TODO("Not yet implemented")
    }
}