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
import com.lavinou.startpoint.auth.biometric.BiometricIdentifier
import com.lavinou.startpoint.auth.biometric.core.getActivityOrNull
import com.lavinou.startpoint.auth.biometric.presentation.action.BiometricAction
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.navigation.nextActionNavigateTo
import com.lavinou.startpoint.auth.providerOrNull
import kotlinx.serialization.Serializable

@Serializable
object BiometricSignIn

@Serializable
object BiometricRegistration

internal fun NavGraphBuilder.biometricGraph(
    auth: SPAuth,
    navController: NavController
) {

    val biometric = auth.providerOrNull(Biometric)
    biometric?.let {
        dialog<BiometricSignIn> {
            val onBack: () -> Unit = {
                navController.popBackStack()
            }
            BackHandler(onBack = onBack)
            val activity = LocalContext.current.getActivityOrNull() as FragmentActivity
            biometric.BiometricPromptDialog(
                activity,
                onNextAction = { action ->
                    when (action) {
                        is SPAuthNextAction.NavigateTo -> {
                            navController.nextActionNavigateTo(action)
                        }

                        else -> Unit
                    }
                },
                onBiometricResult = { result ->
                    biometric.viewModel.dispatch(
                        BiometricAction.Authenticate(
                            result = result,
                            deviceId = BiometricIdentifier.deviceId(activity),
                            auth = auth
                        )
                    )
                }
            )
        }

        composable<BiometricRegistration> {
            val fragment = LocalContext.current.getActivityOrNull() as FragmentActivity
            biometric.BiometricRegistrationScreen(
                fragment = fragment,
                onCancel = {
                    navController.popBackStack()
                },
                onNavigate = { route ->
                    navController.nextActionNavigateTo(route)
                }
            )
        }
    }

}

