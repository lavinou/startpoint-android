package com.lavinou.startpoint.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

/**
 * Serializable object representing the main content entry point for the client application.
 * This object serves as a marker or identifier for navigating to or managing the primary
 * content within the application.
 *
 * Being serializable allows `MainContent` to be used in data transfer, persistence,
 * and state restoration processes.
 */
@Serializable
object MainContent

@Composable
internal fun StartPointNavHost(
    graphs: List<NavGraphBuilder.(NavHostController) -> Unit> = emptyList(),
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = MainContent,
        modifier = modifier
    ) {

        composable<MainContent> {
            content()
        }

        graphs.forEach { it(navHostController) }
    }
}