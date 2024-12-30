package com.lavinou.startpoint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

/**
 * Composable function that remembers and initializes a StartPoint instance for the current composition.
 * This function creates and configures the StartPoint with the provided configuration block, ensuring
 * it persists across recompositions.
 *
 * @param configuration A lambda used to configure the StartPoint instance. This block is applied
 *                      to the StartPointConfiguration during initialization.
 * @return A remembered StartPoint instance that manages navigation and plugin integration within
 *         the application.
 */
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