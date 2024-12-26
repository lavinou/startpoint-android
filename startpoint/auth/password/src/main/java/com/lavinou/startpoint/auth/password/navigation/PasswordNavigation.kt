package com.lavinou.startpoint.auth.password.navigation

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
            ForgotPasswordContent(
                navHostController = navHostController
            )
        }

        composable<ConfirmPasswordReset>(deepLinks = listOf()) {
            ConfirmPasswordResetContent(navHostController = navHostController)
        }
    }

}