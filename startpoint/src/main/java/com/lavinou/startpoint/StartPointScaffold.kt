package com.lavinou.startpoint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.lavinou.startpoint.navigation.StartPointNavHost


@OptIn(ExperimentalMaterial3Api::class)
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