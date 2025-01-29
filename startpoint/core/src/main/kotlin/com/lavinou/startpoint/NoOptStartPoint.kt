package com.lavinou.startpoint

import android.content.Context
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.Attributes

internal class NoOptStartPoint : StartPoint {

    override val context: Context
        get() = error("Using No Opt StartPoint")

    override val navigation: NavHostController
        get() = error("Using No Opt StartPoint")

    override val attributes: Attributes
        get() = Attributes(false)

    override val config: StartPointConfiguration
        get() = error("Using No Opt StartPoint")

    override val installedPlugins: List<StartPointPlugin<*, *>>
        get() = emptyList()

    override fun addInstalledPlugin(plugin: StartPointPlugin<*, *>) {
        // no-op
    }
}