package com.lavinou.startpoint.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes

internal val PROVIDER_INSTALLED_LIST = AttributeKey<Attributes>("AuthProviderRegistry")


interface SPAuthProvider<out TConfig : Any, TProvider : Any> {

    val key: AttributeKey<TProvider>

    val graph: NavGraphBuilder.(NavHostController) -> Unit

    fun prepare(block: TConfig.() -> Unit): TProvider

    fun install(plugin: TProvider, scope: SPAuth)
}

public fun <B : Any, F : Any> SPAuth.providerOrNull(plugin: SPAuthProvider<B, F>): F? =
    attributes.getOrNull(PROVIDER_INSTALLED_LIST)?.getOrNull(plugin.key)

public fun <B : Any, F : Any> SPAuth.provider(plugin: SPAuthProvider<B, F>): F {
    return providerOrNull(plugin) ?: throw IllegalStateException(
        "Plugin $plugin is not installed. Consider using `install(${plugin.key})` in client config first."
    )
}

public fun <B : Any, F : Any> SPAuth.containsAnyBut(plugin: SPAuthProvider<B, F>): Boolean {
    return attributes.getOrNull(PROVIDER_INSTALLED_LIST)?.allKeys?.any { plugin.key != it } ?: false
}

