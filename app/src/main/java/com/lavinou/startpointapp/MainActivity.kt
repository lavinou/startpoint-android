package com.lavinou.startpointapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.StartPointScaffold
import com.lavinou.startpoint.rememberStartPoint
import com.lavinou.startpointapp.auth.installAuth
import com.lavinou.startpointapp.navigation.AppNavHost
import com.lavinou.startpointapp.ui.theme.StartPointAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()

            val startPoint = rememberStartPoint {
                installAuth(this@MainActivity)
            }

            StartPointAppTheme {
                StartPointScaffold(startPoint = startPoint) {
                    AppNavHost(
                        startPoint,
                        navHostController,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}