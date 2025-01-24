package com.lavinou.startpoint.auth.social.facebook

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

class FaceBook {

    @Composable
    internal fun Content(
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Login With FaceBook")
        }
    }

    @StartPointDsl
    companion object Plugin : SPAuthProvider<FaceBookConfiguration, FaceBook> {

        private var currentPlugin: FaceBook? = null

        override val key: AttributeKey<FaceBook>
            get() = AttributeKey("FaceBook")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = {

            }

        override fun install(provider: FaceBook, scope: SPAuth) {
            currentPlugin = provider
        }

        override fun prepare(block: FaceBookConfiguration.() -> Unit): FaceBook {
            return FaceBookConfiguration().apply(block).build()
        }

    }

}