package com.lavinou.startpoint.auth.biometric.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.biometric.Biometric
import com.lavinou.startpoint.auth.biometric.core.getActivityOrNull
import com.lavinou.startpoint.auth.biometric.credentials.BiometricCredential
import com.lavinou.startpoint.auth.providerOrNull
import kotlinx.serialization.Serializable

@Serializable
object BiometricSignIn

@Serializable
object BiometricRegistration

fun NavGraphBuilder.biometricGraph(
    auth: SPAuth,
    navController: NavController
) {

    val biometric = auth.providerOrNull(Biometric)
    biometric?.let {
        dialog<BiometricSignIn> {
            val onBack: () -> Unit = {
                biometric.onCancelRoute?.let {
                    navController.popBackStack()
                    navController.navigate(it)
                }
            }
            BackHandler(onBack = onBack)
            val activity = LocalContext.current.getActivityOrNull() as FragmentActivity
            biometric.BiometricPromptDialog(
                activity,
                navController,
                onSuccess = { id, sign ->
                    val credential = BiometricCredential(id, sign)
                    auth.authenticate(credential)
                },
                onCancel = onBack
            )
        }

        composable<BiometricRegistration> {
            biometric.BiometricRegistrationScreen(
                navHostController = navController
            )
        }
    }

}

