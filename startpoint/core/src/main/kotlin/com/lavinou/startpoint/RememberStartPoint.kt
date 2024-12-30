package com.lavinou.startpoint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberStartPoint(
    configuration: StartPointConfiguration.() -> Unit
): StartPoint {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val startPoint = remember {
        AndroidStartPoint(
            context,
            navHostController,
            configuration
        )
    }
    return startPoint
}