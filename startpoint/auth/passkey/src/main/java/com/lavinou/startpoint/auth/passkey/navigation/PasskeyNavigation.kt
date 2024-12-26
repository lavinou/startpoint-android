package com.lavinou.startpoint.auth.passkey.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.passkey.Passkey
import com.lavinou.startpoint.auth.passkey.presentation.PasskeyOtherWaysToSignUpContent
import com.lavinou.startpoint.auth.providerOrNull
import kotlinx.serialization.Serializable

@Serializable
object PasskeySignIn

@Serializable
object PasskeySignUp

@Serializable
internal object PasskeyOtherWaysToSignUp

internal fun NavGraphBuilder.passkey(
    startPointAuth: SPAuth,
    navHostController: NavHostController
) {

    val passkey = startPointAuth.providerOrNull(Passkey)

    passkey?.let {
        composable<PasskeySignIn> {
            passkey.SignInContent(
                navHostController = navHostController,
                startPointAuth = startPointAuth
            )
        }

        composable<PasskeySignUp> {
            passkey.SignUpContent(
                navHostController = navHostController,
                startPointAuth = startPointAuth
            )
        }

        composable<PasskeyOtherWaysToSignUp> {
            PasskeyOtherWaysToSignUpContent(
                navHostController = navHostController,
                startPointAuth = startPointAuth
            )
        }
    }


}