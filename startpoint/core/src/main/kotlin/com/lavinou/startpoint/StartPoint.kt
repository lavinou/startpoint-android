package com.lavinou.startpoint

import android.content.Context
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.Attributes

/**
 * Core interface representing the main entry point for the StartPoint application.
 * This interface defines essential properties and methods required for configuring,
 * navigating, and extending the StartPoint platform through plugins.
 */
interface StartPoint {

    /**
     * Application or activity context required for initializing and interacting with the framework.
     */
    val context: Context

    /**
     * Navigation controller for managing navigation within the StartPoint application.
     */
    val navigation: NavHostController

    /**
     * Configuration object containing settings and parameters for initializing StartPoint.
     */
    val config: StartPointConfiguration

    /**
     * Attributes associated with the current StartPoint instance. These can define metadata
     * or contextual information relevant to the app.
     */
    val attributes: Attributes

    /**
     * List of plugins that have been installed and integrated into the StartPoint application.
     * Each plugin extends the functionality of StartPoint.
     */
    val installedPlugins: List<StartPointPlugin<*, *>>

    /**
     * Installs a new plugin into the StartPoint application. This method allows for
     * dynamically adding and configuring plugins to extend or modify core functionality.
     *
     * @param plugin The plugin instance to be added.
     */
    fun addInstalledPlugin(plugin: StartPointPlugin<*, *>)
}