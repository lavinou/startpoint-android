package com.lavinou.startpoint.auth

import android.content.Context
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.auth.backend.SPUserSessionBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.storage.DefaultSPAuthStorage
import com.lavinou.startpoint.auth.storage.SPAuthStorage
import com.lavinou.startpoint.dsl.StartPointDsl
import kotlin.reflect.KClass

/**
 * Configuration class for SPAuth, responsible for setting up authentication behavior,
 * defining routes, and managing user sessions.
 */
@StartPointDsl
class SPAuthConfiguration internal constructor(
    private val context: Context,
    public val storage: SPAuthStorage = DefaultSPAuthStorage(context)
){

    /**
     * The title displayed during the authentication process.
     */
    public var title: String = ""

    /**
     * The route or action triggered when the sign-in button is pressed.
     */
    public var signInButtonRoute: Any = Any()

    /**
     * The route or action triggered when the sign-up button is pressed.
     */
    public var signUpButtonRoute: Any = Any()

    /**
     * A suspendable callback function invoked upon successful authentication.
     */
    public var onComplete: (suspend (SPAuthToken) -> Unit)? = null

    /**
     * An optional image associated with the authentication process.
     */
    public var image: Any? = null

    /**
     * A map that holds user session managers by user type.
     */
    public val userSessions = mutableMapOf<KClass<*>, SPAuthUserSession<*>>()

    private val providers: MutableMap<AttributeKey<*>, (SPAuth) -> Unit> = mutableMapOf()

    private val providerConfigurations: MutableMap<AttributeKey<*>, Any.() -> Unit> = mutableMapOf()

    /**
     * Configures a user session backend for a specific user type.
     * This method sets up session handling logic for users, storing their session data.
     *
     * @param backend The backend responsible for managing the user session.
     */
    public inline fun <reified TUser : SPAuthUser<*>> setUserSessionBackend(
        backend: SPUserSessionBackend<TUser>
    ) {
        val storage = storage
        userSessions[TUser::class] = SPAuthUserSession(
            storage = storage,
            backend = backend
        )
    }

    /**
     * Adds a new authentication provider to the SPAuth configuration.
     * Providers enable different authentication mechanisms to integrate with SPAuth.
     *
     * @param provider The authentication provider to add.
     * @param configure An optional lambda to configure the provider.
     */
    public fun <TBuilder : Any, TPlugin : Any> addProvider(
        provider: SPAuthProvider<TBuilder, TPlugin>,
        configure: TBuilder.() -> Unit = {}
    ) {
        val previousConfigBlock = providerConfigurations[provider.key]
        providerConfigurations[provider.key] = {
            previousConfigBlock?.invoke(this)

            @Suppress("UNCHECKED_CAST")
            (this as TBuilder).configure()
        }

        if (providers.containsKey(provider.key)) return

        providers[provider.key] = { scope ->
            val attributes =
                scope.attributes.computeIfAbsent(PROVIDER_INSTALLED_LIST) { Attributes(concurrent = true) }
            val config = providerConfigurations[provider.key]!!
            val pluginData = provider.prepare(config)

            provider.install(pluginData, scope)
            scope.addInstalledProvider(provider)
            attributes.put(provider.key, pluginData)
        }
    }

    internal fun install(client: SPAuth) {
        providers.values.forEach {
            client.apply(it)
        }
    }

    internal fun build(): SPAuth {
        return SPAuth(config = this)
    }
}

/**
 * Creates a preview instance of SPAuth for testing and demonstration purposes.
 *
 * @param context The application context.
 * @return A preview instance of SPAuth with default configurations.
 */
public fun authPreview(context: Context): SPAuth {
    val config = SPAuthConfiguration(context)
    config.title = "Preview"
    return config.build()
}