package com.lavinou.startpoint

import com.lavinou.startpoint.attribute.Attributes

internal class NoOptStartPoint : StartPoint {

    override val attributes: Attributes
        get() = Attributes(false)

    override val config: StartPointConfiguration
        get() = StartPointConfiguration()

    override val installedPlugins: List<StartPointPlugin<*, *>>
        get() = emptyList()

    override fun addInstalledPlugin(plugin: StartPointPlugin<*, *>) {
        // no-op
    }
}