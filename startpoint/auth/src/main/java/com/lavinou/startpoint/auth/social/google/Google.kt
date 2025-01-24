package com.lavinou.startpoint.auth.social.google

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.dsl.StartPointDsl

class Google {

    @Composable
    internal fun Content(
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Login With Google")
        }
    }

    @StartPointDsl
    companion object Plugin : SPAuthProvider<GoogleConfiguration, Google> {

        private var currentPlugin: Google? = null

        override val key: AttributeKey<Google>
            get() = AttributeKey("Google")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = {

            }

        override fun install(provider: Google, scope: SPAuth) {
            currentPlugin = provider
        }

        override fun prepare(block: GoogleConfiguration.() -> Unit): Google {
            return GoogleConfiguration().apply(block).build()
        }

    }

}