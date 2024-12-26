package com.lavinou.startpoint

import com.lavinou.startpoint.attribute.Attributes

interface StartPoint {

    val config: StartPointConfiguration

    val attributes: Attributes

    val installedPlugins: List<StartPointPlugin<*, *>>

    fun addInstalledPlugin(plugin: StartPointPlugin<*, *>)
}