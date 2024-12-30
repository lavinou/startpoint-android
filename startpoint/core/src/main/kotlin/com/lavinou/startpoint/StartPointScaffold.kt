package com.lavinou.startpoint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.lavinou.startpoint.navigation.StartPointNavHost

/**
 * Composable function that sets up the main scaffold structure for a StartPoint application.
 * This function integrates the navigation and plugin system within a standard Material Scaffold,
 * providing a consistent top-level layout for the application.
 *
 * @param startPoint The StartPoint instance managing the application's configuration, navigation,
 *                   and installed plugins.
 * @param content A composable lambda that represents the main content of the scaffold.
 *                This content will be displayed within the scaffold, below the top bar and above
 *                the bottom bar.
 */
@Composable
fun StartPointScaffold(
    startPoint: StartPoint,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {

        },
        bottomBar = {

        }
    ) { paddingValues ->
        CompositionLocalProvider(LocalStartPoint provides startPoint) {
            StartPointNavHost(
                graphs = startPoint.installedPlugins.map { it.graph },
                navHostController = startPoint.navigation,
                modifier = Modifier.padding(paddingValues),
                content = content
            )
        }
    }
}