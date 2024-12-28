package com.lavinou.startpoint

import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.Attributes

interface StartPoint {

    val navigation: NavHostController

    val config: StartPointConfiguration

    val attributes: Attributes

    val installedPlugins: List<StartPointPlugin<*, *>>

    fun addInstalledPlugin(plugin: StartPointPlugin<*, *>)

}