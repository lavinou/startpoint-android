package com.lavinou.startpointapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.StartPointScaffold
import com.lavinou.startpoint.rememberStartPoint
import com.lavinou.startpointapp.auth.installAuth
import com.lavinou.startpointapp.navigation.AppNavHost
import com.lavinou.startpointapp.ui.theme.StartPointAppTheme

class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()

            val startPoint = rememberStartPoint {
                installAuth(this@MainActivity)
            }

            StartPointAppTheme {
                StartPointScaffold(startPoint = startPoint) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text("Home")
                                }
                            )
                        }
                    ) { paddingValues ->
                        AppNavHost(
                            startPoint,
                            navHostController,
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }

                }
            }
        }
    }
}