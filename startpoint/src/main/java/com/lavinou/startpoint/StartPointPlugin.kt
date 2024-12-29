package com.lavinou.startpoint

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.dsl.StartPointDsl

internal val PLUGIN_INSTALLED_LIST = AttributeKey<Attributes>("ApplicationPluginRegistry")

@StartPointDsl
public interface StartPointPlugin<out TConfig : Any, TPlugin : Any> {

    public val key: AttributeKey<TPlugin>

    public val graph: NavGraphBuilder.(NavHostController) -> Unit

    public fun prepare(block: TConfig.() -> Unit = {}, scope: StartPoint): TPlugin

    public fun install(plugin: TPlugin, scope: StartPoint)

}

public fun <B : Any, F : Any> StartPoint.pluginOrNull(plugin: StartPointPlugin<B, F>): F? =
    attributes.getOrNull(PLUGIN_INSTALLED_LIST)?.getOrNull(plugin.key)

public fun <B : Any, F : Any> StartPoint.plugin(plugin: StartPointPlugin<B, F>): F {
    return pluginOrNull(plugin) ?: throw IllegalStateException(
        "Plugin $plugin is not installed. Consider using `install(${plugin.key})` in client config first."
    )
}