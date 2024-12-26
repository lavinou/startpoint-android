package com.lavinou.startpoint.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
internal fun StartPointNavHost(
    route: String,
    startDestination: String,
    graphs: List<NavGraphBuilder.(NavHostController) -> Unit> = emptyList(),
    navHostController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        route = route,
        modifier = modifier
    ) {
        graphs.forEach {
            it(navHostController)
        }
        builder()
    }
}