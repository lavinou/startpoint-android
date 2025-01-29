package com.lavinou.startpoint

import android.content.Context
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.Attributes

internal class AndroidStartPoint(
    override val context: Context,
    private val navHostController: NavHostController,
    configuration: StartPointConfiguration.() -> Unit
) : StartPoint {

    private val _installedPlugins: MutableList<StartPointPlugin<*, *>> = mutableListOf()

    override val config: StartPointConfiguration = StartPointConfiguration(current = this)

    override val attributes: Attributes = Attributes(concurrent = true)

    override val installedPlugins: List<StartPointPlugin<*, *>> = _installedPlugins

    override val navigation: NavHostController
        get() = navHostController

    init {
        configuration.invoke(config)
        config.install(this)
    }

    override fun addInstalledPlugin(plugin: StartPointPlugin<*, *>) {
        _installedPlugins.add(plugin)
    }
}