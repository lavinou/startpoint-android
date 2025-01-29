package com.lavinou.startpointapp.analytics

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.StartPoint
import com.lavinou.startpoint.StartPointPlugin
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.dsl.StartPointDsl

class AnalyticsConfiguration {

    internal fun build(): Analytics {
        Log.d("AnalyticsConfiguration", "Building")
        return Analytics()
    }
}

class Analytics {



    @StartPointDsl
    companion object Plugin : StartPointPlugin<AnalyticsConfiguration, Analytics> {

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = {}

        override val key: AttributeKey<Analytics>
            get() = AttributeKey("Analytics")

        override fun install(plugin: Analytics, scope: StartPoint) {

        }

        override fun prepare(
            block: AnalyticsConfiguration.() -> Unit,
            scope: StartPoint
        ): Analytics {
            return AnalyticsConfiguration().apply(block).build()
        }

    }
}