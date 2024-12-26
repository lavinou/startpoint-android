package com.lavinou.startpoint

import android.content.Context
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

class AndroidStartPoint constructor(
    configuration: StartPointConfiguration.() -> Unit
) : StartPoint {

    private val _installedPlugins: MutableList<StartPointPlugin<*, *>> = mutableListOf()

    override val config: StartPointConfiguration = StartPointConfiguration()

    override val attributes: Attributes = Attributes(concurrent = true)

    override val installedPlugins: List<StartPointPlugin<*, *>> = _installedPlugins

    init {
        configuration.invoke(config)
        config.install(this)
    }

    override fun addInstalledPlugin(plugin: StartPointPlugin<*, *>) {
        _installedPlugins.add(plugin)
    }
}