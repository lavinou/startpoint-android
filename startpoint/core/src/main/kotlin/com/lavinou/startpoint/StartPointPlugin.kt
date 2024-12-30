package com.lavinou.startpoint

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

internal val PLUGIN_INSTALLED_LIST = AttributeKey<Attributes>("ApplicationPluginRegistry")

/**
 * Defines a plugin that can be installed into the StartPoint framework to extend its functionality.
 * Plugins encapsulate additional features or behaviors that can be dynamically added to the application.
 *
 * @param TConfig The type representing the configuration block for the plugin.
 * @param TPlugin The type representing the plugin instance after preparation.
 */
@StartPointDsl
public interface StartPointPlugin<out TConfig : Any, TPlugin : Any> {

    /**
     * Unique key used to identify and manage the plugin within the StartPoint framework.
     */
    public val key: AttributeKey<TPlugin>

    /**
     * Defines the navigation graph associated with this plugin.
     * This graph determines how the plugin integrates with the application's navigation flow.
     */
    public val graph: NavGraphBuilder.(NavHostController) -> Unit

    /**
     * Prepares the plugin for installation by applying the provided configuration block.
     * This method initializes the plugin and returns the fully configured instance.
     *
     * @param block Configuration block to customize the plugin during preparation.
     * @param scope The StartPoint instance in which the plugin is being prepared.
     * @return The prepared and initialized plugin instance.
     */
    public fun prepare(block: TConfig.() -> Unit = {}, scope: StartPoint): TPlugin

    /**
     * Installs the plugin into the specified StartPoint instance.
     * This method finalizes the plugin's integration and registers it with the StartPoint scope.
     *
     * @param plugin The plugin instance to be installed.
     * @param scope The StartPoint instance where the plugin will be installed.
     */
    public fun install(plugin: TPlugin, scope: StartPoint)

}

/**
 * Retrieves an installed plugin from the StartPoint instance, returning null if the plugin is not found.
 *
 * @param plugin The plugin to retrieve.
 * @return The installed plugin instance or null if the plugin is not installed.
 */
public fun <B : Any, F : Any> StartPoint.pluginOrNull(plugin: StartPointPlugin<B, F>): F? =
    attributes.getOrNull(PLUGIN_INSTALLED_LIST)?.getOrNull(plugin.key)

/**
 * Retrieves an installed plugin from the StartPoint instance.
 * Throws an IllegalStateException if the plugin is not installed.
 *
 * @param plugin The plugin to retrieve.
 * @return The installed plugin instance.
 * @throws IllegalStateException if the plugin is not installed.
 */
public fun <B : Any, F : Any> StartPoint.plugin(plugin: StartPointPlugin<B, F>): F {
    return pluginOrNull(plugin) ?: throw IllegalStateException(
        "Plugin $plugin is not installed. Consider using `install(${plugin.key})` in client config first."
    )
}