package com.lavinou.startpoint.auth.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.image.AsyncImage
import com.lavinou.startpoint.navigation.MainContent
import kotlinx.serialization.Serializable

@Serializable
internal object AuthMain

@Serializable
object StartPointAuthRoute

internal fun NavGraphBuilder.auth(
    auth: SPAuth,
    navHostController: NavHostController
) {
    navigation<AuthMain>(
        startDestination = StartPointAuthRoute,
    ) {

        auth.installedProvider.map {
            it.graph
        }.forEach { graph ->
            graph(navHostController)
        }

        composable<StartPointAuthRoute> {

            val onBack: () -> Unit = {
                auth.onCancel?.invoke()
                navHostController.popBackStack(MainContent, inclusive = false)
            }

            BackHandler(onBack = onBack)
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {

                AuthHeader(
                    onBack = onBack,
                    title = auth.title
                )

                AsyncImage(
                    model = auth.image,
                    modifier = Modifier.fillMaxWidth()
                )

                Column {

                    Button(
                        onClick = { navHostController.navigate(auth.signUpButtonRoute) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Sign Up")
                    }

                    Button(
                        onClick = { navHostController.navigate(auth.signInButtonRoute) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Text(text = "Sign In")
                    }
                }
            }
        }

    }
}