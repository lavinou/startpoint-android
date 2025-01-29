package com.lavinou.startpoint.auth.password.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.password.Password
import com.lavinou.startpoint.auth.password.presentation.ConfirmPasswordResetContent
import com.lavinou.startpoint.auth.password.presentation.ForgotPasswordContent
import com.lavinou.startpoint.auth.providerOrNull
import kotlinx.serialization.Serializable

@Serializable
object PasswordSignIn

@Serializable
object PasswordSignUp

@Serializable
internal object ForgotPassword

@Serializable
internal object ConfirmPasswordReset

@Serializable
internal object VerifyEmail

internal fun NavGraphBuilder.password(
    startPointAuth: SPAuth,
    navHostController: NavHostController
) {

    val password = startPointAuth.providerOrNull(Password)

    password?.let {
        composable<PasswordSignIn> {
            password.SignInContent(
                navHostController = navHostController,
                startPointAuth = startPointAuth
            )
        }

        composable<PasswordSignUp> {
            password.SignUpContent(
                navHostController = navHostController
            )
        }

        composable<ForgotPassword> {
            val state by password.passwordViewModel.state.collectAsState()
            ForgotPasswordContent(
                navHostController = navHostController,
                state = state,
                onDispatchAction = password.passwordViewModel::dispatch,
                isValid = password.passwordViewModel::isValid
            )
            password.ForgotPasswordContent()
        }

        composable<ConfirmPasswordReset>(deepLinks = listOf()) {
            ConfirmPasswordResetContent(navHostController = navHostController)
            password.ConfirmPasswordResetContent()
        }

        composable<VerifyEmail> {
            password.VerifyEmailContent()
        }
    }
}