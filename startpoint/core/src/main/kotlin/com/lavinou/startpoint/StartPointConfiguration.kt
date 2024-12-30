package com.lavinou.startpoint

import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

/**
 * Constructs a new instance of StartPointConfiguration.
 * This class manages the registration and configuration of plugins for the StartPoint application.
 * Plugins are installed and configured dynamically, allowing flexible extension of core functionality.
 */
@StartPointDsl
class StartPointConfiguration internal constructor(){

    private val plugins: MutableMap<AttributeKey<*>, (StartPoint) -> Unit> = mutableMapOf()
    private val pluginConfigurations: MutableMap<AttributeKey<*>, Any.() -> Unit> = mutableMapOf()

    /**
     * Installs a plugin into the StartPoint configuration.
     * This method registers the plugin and applies configuration to it if provided.
     *
     * @param plugin The plugin instance to be installed.
     * @param configure Optional configuration block to customize the plugin during installation.
     */
    public fun <TBuilder : Any, TPlugin : Any> install(
        plugin: StartPointPlugin<TBuilder, TPlugin>,
        configure: TBuilder.() -> Unit = {}
    ) {
        val previousConfigBlock = pluginConfigurations[plugin.key]
        pluginConfigurations[plugin.key] = {
            previousConfigBlock?.invoke(this)

            @Suppress("UNCHECKED_CAST")
            (this as TBuilder).configure()
        }

        if (plugins.containsKey(plugin.key)) return

        plugins[plugin.key] = { scope ->
            val attributes =
                scope.attributes.computeIfAbsent(PLUGIN_INSTALLED_LIST) { Attributes(concurrent = true) }
            val config = scope.config.pluginConfigurations[plugin.key]!!
            val pluginData = plugin.prepare(config, scope)

            plugin.install(pluginData, scope)
            scope.addInstalledPlugin(plugin)
            attributes.put(plugin.key, pluginData)
        }
    }

    /**
     * Applies all installed plugins to the specified StartPoint instance.
     * This ensures that every plugin registered in the configuration is initialized
     * and integrated into the provided StartPoint client.
     *
     * @param client The StartPoint instance to install the plugins into.
     */
    internal fun install(client: StartPoint) {
        plugins.values.forEach { client.apply(it) }
    }
}