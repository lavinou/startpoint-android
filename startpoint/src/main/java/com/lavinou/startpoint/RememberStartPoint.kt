package com.lavinou.startpoint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberStartPoint(
    configuration: StartPointConfiguration.() -> Unit
): StartPoint {
    val navHostController = rememberNavController()
    val startPoint = remember {
        AndroidStartPoint(
            navHostController,
            configuration
        )
    }
    return startPoint
}