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
import com.lavinou.startpoint.auth.protectedRoute
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
        startDestination = "test",
        route = "home",
        modifier = modifier
    ) {
        composable(route = "home-main") {
            navHostController.protectedRoute<AppUser> {
                Column {
                    Text(text = "Hello ${status.value.user?.username}")
                    Button(onClick = {
                        scope.launch {
                            userSession.logout()
                        }
                    }) {
                        Text(text = "Logout")
                    }

                    Button(onClick = {
                        scope.launch {
                            navHostController.navigate("test2")
                        }
                    }) {
                        Text(text = "Test2")
                    }
                }
            }
        }

        composable(route = "test2") {
            navHostController.protectedRoute<AppUser> {
                Column {
                    Text("Another protected resource")
                    Button(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Text("Pop back")
                    }
                }
            }
        }

        composable(route = "test") {
            Column {
                Text("This is for testing")
                Button(onClick = {
                    navHostController.navigate("home-main")
                }) {
                    Text(text = "Go to Home")
                }
                Button(onClick = {
                    startPoint.navigation.navigate(StartPointAuthRoute)
                }) {
                    Text(text = "Go to Auth")
                }

                Button(onClick = {
                    navHostController.navigate("test2")
                }) {
                    Text(text = "Test 2")
                }
            }
        }
    }
}