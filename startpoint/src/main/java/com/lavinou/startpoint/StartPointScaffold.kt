package com.lavinou.startpoint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.navigation.StartPointNavHost


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartPointScaffold(
    startPoint: StartPoint,
    navHostController: NavHostController = rememberNavController(),
    route: String = "home",
    startDestination: String = "home-main",
    content: NavGraphBuilder.() -> Unit
) {
    Scaffold(
        topBar = {

        },
        bottomBar = {

        }
    ) { paddingValues ->
        CompositionLocalProvider(LocalStartPoint provides startPoint) {
            StartPointNavHost(
                route = route,
                startDestination = startDestination,
                graphs = startPoint.installedPlugins.map { it.graph },
                navHostController = navHostController,
                modifier = Modifier.padding(paddingValues),
                builder = content
            )
        }

    }
}