package com.lavinou.startpointapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.StartPointScaffold
import com.lavinou.startpoint.auth.navigation.StartPointAuthRoute
import com.lavinou.startpoint.auth.userSession
import com.lavinou.startpoint.rememberStartPoint
import com.lavinou.startpointapp.auth.installAuth
import com.lavinou.startpointapp.auth.model.AppUser
import com.lavinou.startpointapp.ui.theme.StartPointAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()
            val scope = rememberCoroutineScope()
            val startPoint = rememberStartPoint {
                installAuth(this@MainActivity)
            }

            val userSession = startPoint.userSession<AppUser>()
            val status = userSession.userFlow.collectAsState(initial = AppUser.AnonymousUser())

            StartPointAppTheme {
                // A surface container using the 'background' color from the theme
                StartPointScaffold(
                    startPoint = startPoint
                ) {
                    NavHost(navHostController, startDestination = "home-main", route = "home") {
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
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StartPointAppTheme {
        Greeting("Android")
    }
}