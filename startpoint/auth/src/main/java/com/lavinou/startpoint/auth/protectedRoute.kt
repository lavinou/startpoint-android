package com.lavinou.startpoint.auth

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.lavinou.startpoint.LocalStartPoint
import com.lavinou.startpoint.auth.navigation.StartPointAuthRoute
import com.lavinou.startpoint.plugin

@Composable
inline fun <reified T : SPAuthUser<*>> NavController.protectedRoute(
    content: @Composable () -> Unit
) {

    val startPoint = LocalStartPoint.current
    val auth = startPoint.plugin(SPAuth)
    val isAuthenticated by startPoint.userSession<T>().isAuthenticated.collectAsState()
    val context = (LocalContext.current as? Activity)

    if (isAuthenticated != startPoint.userSession<T>().isLoggedIn()) {
        Column {
            CircularProgressIndicator()
        }
    } else if (isAuthenticated.not()) {
        LaunchedEffect(Unit) {
            auth.setOnCancel {

                if (auth.existOnUserCancel) {
                    previousBackStackEntry?.destination?.route?.let {
                        popBackStack(it, inclusive = true)
                    }
                }

                previousBackStackEntry?.let {
                    popBackStack()
                } ?: run {
                    context?.finish()
                }
            }
            startPoint.navigation.navigate(StartPointAuthRoute) {
                launchSingleTop = true
            }
        }
    } else {
        content()
    }
}
