package com.lavinou.startpoint

import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class StartPointConfiguration {

    private val plugins: MutableMap<AttributeKey<*>, (StartPoint) -> Unit> = mutableMapOf()
    private val pluginConfigurations: MutableMap<AttributeKey<*>, Any.() -> Unit> = mutableMapOf()

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
            val pluginData = plugin.prepare(config)

            plugin.install(pluginData, scope)
            scope.addInstalledPlugin(plugin)
            attributes.put(plugin.key, pluginData)
        }
    }

    public fun install(client: StartPoint) {
        plugins.values.forEach { client.apply(it) }
    }
}