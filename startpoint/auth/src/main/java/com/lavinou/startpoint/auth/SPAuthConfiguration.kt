package com.lavinou.startpoint.auth

import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.auth.backend.SPUserSessionBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.storage.SPAuthStorage
import com.lavinou.startpoint.dsl.StartPointDsl
import kotlin.reflect.KClass

@StartPointDsl
class SPAuthConfiguration {

    var title: String = ""

    var signInButtonRoute: Any = Any()

    var signUpButtonRoute: Any = Any()

    var onComplete: (suspend (SPAuthToken) -> Unit)? = null

    var storage: SPAuthStorage? = null
        private set

    val userSessions = mutableMapOf<KClass<*>, SPAuthUserSession<*>>()

    fun setStorage(authStorage: SPAuthStorage) {
        storage = authStorage
    }

    inline fun <reified TUser : SPAuthUser<*>> setUserSessionBackend(
        backend: SPUserSessionBackend<TUser>
    ) {
        val storage = storage ?: error("Storage is not set. setStorage to enable user session.")
        userSessions[TUser::class] = SPAuthUserSession(
            storage = storage,
            backend = backend
        )
    }

    inline fun <reified TUser : SPAuthUser<*>> userSession(): SPAuthUserSession<TUser> {
        val manager = userSessions[TUser::class]
        return if (manager is SPAuthUserSession<*>) {
            @Suppress("UNCHECKED_CAST")
            manager as SPAuthUserSession<TUser>
        } else {
            throw IllegalStateException("UserManager for ${TUser::class} not found")
        }
    }

    private val providers: MutableMap<AttributeKey<*>, (SPAuth) -> Unit> = mutableMapOf()
    private val providerConfigurations: MutableMap<AttributeKey<*>, Any.() -> Unit> = mutableMapOf()

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
            val config = scope.config.providerConfigurations[provider.key]!!
            val pluginData = provider.prepare(config)

            provider.install(pluginData, scope)
            scope.addInstalledProvider(provider)
            attributes.put(provider.key, pluginData)
        }
    }

    public fun install(client: SPAuth) {
        providers.values.forEach {
            client.apply(it)
        }
    }

    internal fun build(): SPAuth {

        return SPAuth(
            title = title,
            signInButtonRoute = signInButtonRoute,
            signUpButtonRoute = signUpButtonRoute,
            config = this
        )
    }

}