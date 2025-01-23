package com.lavinou.startpoint.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes

internal val PROVIDER_INSTALLED_LIST = AttributeKey<Attributes>("AuthProviderRegistry")


/**
 * Interface representing an authentication provider in SPAuth.
 * This allows for the integration of custom authentication logic and flows.
 *
 * @param TConfig The type of the configuration used to set up the provider.
 * @param TProvider The type of the provider instance being created and installed.
 */
public interface SPAuthProvider<out TConfig : Any, TProvider : Any> {

    /**
     * The unique key that identifies the provider instance.
     */
    public val key: AttributeKey<TProvider>

    /**
     * Defines the navigation graph associated with this provider.
     * This is used to handle routes within the authentication flow.
     */
    public val graph: NavGraphBuilder.(NavHostController) -> Unit

    /**
     * Prepares and configures the provider using the specified configuration block.
     *
     * @param block Lambda used to configure the provider instance.
     * @return The configured provider instance.
     */
    public fun prepare(block: TConfig.() -> Unit): TProvider

    /**
     * Installs the configured provider into the SPAuth instance.
     *
     * @param provider The provider instance to install.
     * @param scope The SPAuth instance where the provider will be applied.
     */
    public fun install(provider: TProvider, scope: SPAuth)
}

/**
 * Retrieves a provider instance if it exists within the SPAuth scope.
 *
 * @param provider The authentication provider to search for.
 * @return The provider instance if found, otherwise null.
 */
public fun <B : Any, F : Any> SPAuth.providerOrNull(provider: SPAuthProvider<B, F>): F? =
    attributes.getOrNull(PROVIDER_INSTALLED_LIST)?.getOrNull(provider.key)

/**
 * Retrieves a provider instance, or throws an exception if the provider is not installed.
 *
 * @param provider The authentication provider to retrieve.
 * @return The installed provider instance.
 * @throws IllegalStateException if the provider is not installed.
 */
public fun <B : Any, F : Any> SPAuth.provider(provider: SPAuthProvider<B, F>): F {
    return providerOrNull(provider) ?: throw IllegalStateException(
        "Plugin $provider is not installed. Consider using `install(${provider.key})` in client config first."
    )
}

/**
 * Checks if any provider other than the specified one is installed in SPAuth.
 *
 * @param provider The provider to exclude from the check.
 * @return True if there are other installed providers, false otherwise.
 */
public fun <B : Any, F : Any> SPAuth.containsAnyBut(provider: SPAuthProvider<B, F>): Boolean {
    return attributes.getOrNull(PROVIDER_INSTALLED_LIST)?.allKeys?.any { provider.key != it }
        ?: false
}

