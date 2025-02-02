package com.lavinou.startpoint

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.Attributes

class AndroidStartPoint(
    override val context: Context,
    private val navHostController: NavHostController? = null,
    configuration: StartPointConfiguration.() -> Unit
) : StartPoint {

    private val _installedPlugins: MutableList<StartPointPlugin<*, *>> = mutableListOf()

    private var _navHostController = navHostController

    override val config: StartPointConfiguration = StartPointConfiguration(current = this)

    override val attributes: Attributes = Attributes(concurrent = true)

    override val installedPlugins: List<StartPointPlugin<*, *>> = _installedPlugins

    override val navigation: NavHostController
        get() = _navHostController ?: error("Must attach Nav Host Controller when using StartPointScaffold")

    init {
        configuration.invoke(config)
        config.install(this)
    }

    override fun addInstalledPlugin(plugin: StartPointPlugin<*, *>) {
        _installedPlugins.add(plugin)
    }

    override fun attachNavHostController(controller: NavHostController) {
        _navHostController = controller
    }
}