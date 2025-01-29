package com.lavinou.startpointapp.networking

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.StartPoint
import com.lavinou.startpoint.StartPointPlugin
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.dsl.StartPointDsl

class NetworkingConfiguration {

    internal fun build(): Networking {
        Log.d("AnalyticsConfiguration", "Building")
        return Networking()
    }
}

class Networking {



    @StartPointDsl
    companion object Plugin : StartPointPlugin<NetworkingConfiguration, Networking> {

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = {}

        override val key: AttributeKey<Networking>
            get() = AttributeKey("Networking")

        override fun install(plugin: Networking, scope: StartPoint) {

        }

        override fun prepare(
            block: NetworkingConfiguration.() -> Unit,
            scope: StartPoint
        ): Networking {
            return NetworkingConfiguration().apply(block).build()
        }

    }
}