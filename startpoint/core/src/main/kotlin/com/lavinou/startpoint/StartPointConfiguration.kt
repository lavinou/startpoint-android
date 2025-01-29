package com.lavinou.startpoint

import android.util.Log
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

/**
 * Constructs a new instance of StartPointConfiguration.
 * This class manages the registration and configuration of plugins for the StartPoint application.
 * Plugins are installed and configured dynamically, allowing flexible extension of core functionality.
 */
@StartPointDsl
class StartPointConfiguration internal constructor(private val current: StartPoint) {

    private val plugins: MutableMap<AttributeKey<*>, (StartPoint) -> Unit> = mutableMapOf()
    private val pluginConfigurations: MutableMap<AttributeKey<*>, Any.() -> Unit> = mutableMapOf()
    // Dependency graph
    private val dependencyGraph: MutableMap<AttributeKey<*>, MutableList<AttributeKey<*>>> = mutableMapOf()

    /**
     * Installs a plugin into the StartPoint configuration.
     * This method registers the plugin and applies configuration to it if provided.
     *
     * @param plugin The plugin instance to be installed.
     * @param configure Optional configuration block to customize the plugin during installation.
     */
    public fun <TBuilder : Any, TPlugin : Any> install(
        plugin: StartPointPlugin<TBuilder, TPlugin>,
        dependencies: List<StartPointPlugin<*,*>> = emptyList(),
        configure: TBuilder.(StartPoint) -> Unit = {}
    ) {
        val previousConfigBlock = pluginConfigurations[plugin.key]
        pluginConfigurations[plugin.key] = {
            previousConfigBlock?.invoke(this)

            @Suppress("UNCHECKED_CAST")
            (this as TBuilder).configure(current)
        }

        // Track dependencies in the graph
        dependencyGraph.computeIfAbsent(plugin.key) { mutableListOf() }
        dependencies.forEach { dep ->
            dependencyGraph.computeIfAbsent(dep.key) { mutableListOf() }
            dependencyGraph[plugin.key]!!.add(dep.key)
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
     * @throws IllegalStateException when circular dependency is detected
     */
    internal fun install(client: StartPoint) {
        val installOrder = resolveInstallationOrder() ?: throw IllegalStateException("Circular dependency detected!")
        for (pluginKey in installOrder) {
            Log.i("StartPointConfiguration", "Installing Plugin: ${pluginKey.name}")
            plugins[pluginKey]?.invoke(client) ?: throw IllegalStateException("Plugin ${pluginKey.name} not installed")
        }
    }

    /**
     * Resolves the installation order of plugins using topological sorting.
     * Returns `null` if a circular dependency is detected.
     */
    private fun resolveInstallationOrder(): List<AttributeKey<*>>? {
        val sortedOrder = mutableListOf<AttributeKey<*>>()
        val visited = mutableSetOf<AttributeKey<*>>()
        val visiting = mutableSetOf<AttributeKey<*>>() // For detecting cycles

        fun visit(key: AttributeKey<*>): Boolean {
            if (key in sortedOrder) return true // Already sorted
            if (key in visiting) return false // Circular dependency detected

            visiting.add(key)
            for (dependency in dependencyGraph[key] ?: emptyList()) {
                if (!visit(dependency)) return false
            }
            visiting.remove(key)
            visited.add(key)
            sortedOrder.add(key)
            return true
        }

        for (key in dependencyGraph.keys) {
            if (key !in visited && !visit(key)) return null // Cycle detected
        }

        return sortedOrder
    }
}