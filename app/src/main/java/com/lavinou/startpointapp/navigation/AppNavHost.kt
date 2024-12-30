package com.lavinou.startpointapp.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lavinou.startpoint.StartPoint
import com.lavinou.startpoint.auth.navigation.StartPointAuthRoute
import com.lavinou.startpoint.auth.userSession
import com.lavinou.startpointapp.auth.model.AppUser
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    startPoint: StartPoint,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val userSession = startPoint.userSession<AppUser>()
    val status = userSession.userFlow.collectAsState(initial = AppUser.AnonymousUser())

    NavHost(
        navHostController,
        startDestination = "home-main",
        route = "home",
        modifier = modifier
    ) {
        composable(route = "home-main") {
            when (status.value) {
                is AppUser.AuthenticatedUser -> {
                    Column {
                        Text(text = "Hello ${status.value.user?.username}")
                        Button(onClick = {
                            scope.launch {
                                userSession.logout()
                            }
                        }) {
                            Text(text = "Logout")
                        }
                    }
                }

                is AppUser.AnonymousUser -> {
                    Column {
                        Text(text = "Hello World")
                        Button(onClick = {
                            startPoint.navigation.navigate(StartPointAuthRoute)
                        }) {
                            Text(text = "Go to Auth")
                        }
                    }
                }
            }
        }
        composable(route = "test") {
            Column {
                Text("This is for testing")
                Button(onClick = {
                    startPoint.navigation.navigate(StartPointAuthRoute)
                }) {
                    Text(text = "Go to Auth")
                }
            }
        }
    }
}