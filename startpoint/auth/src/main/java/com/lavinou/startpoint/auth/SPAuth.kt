package com.lavinou.startpoint.auth

import androidx.credentials.Credential
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.StartPoint
import com.lavinou.startpoint.StartPointPlugin
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.attribute.Attributes
import com.lavinou.startpoint.auth.backend.SPAuthenticationBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.navigation.StartPointAuthRoute
import com.lavinou.startpoint.auth.navigation.auth
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent
import com.lavinou.startpoint.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SPAuth is the primary class responsible for managing authentication
 * within the StartPoint framework. It facilitates user sign-in, sign-up,
 * and session handling through various backends and configurations.
 */
class SPAuth internal constructor(
    private val config: SPAuthConfiguration
) {

    /**
     * The title associated with this authentication instance,
     * derived from the configuration.
     */
    val title: String
        get() = config.title

    /**
     * An optional image associated with this authentication instance,
     * as specified in the configuration.
     */
    val image: Any?
        get() = config.image

    /**
     * The route or action triggered when the sign-up button is pressed.
     */
    val signUpButtonRoute: Any
        get() = config.signUpButtonRoute

    /**
     * The route or action triggered when the sign-in button is pressed.
     */
    val signInButtonRoute: Any
        get() = config.signInButtonRoute

    /**
     * A map of user sessions managed by this authentication instance.
     */
    val userSessions = config.userSessions

    /**
     * A suspendable function invoked upon successful authentication,
     * passing the generated authentication token.
     */
    val onComplete: (suspend (SPAuthToken) -> Unit)?
        get() {
            return config.onComplete
        }

    /**
     * Exit application on user canceling authentication flow
     */
    val existOnUserCancel: Boolean = config.exitOnUserCancel

    internal val attributes: Attributes = Attributes(concurrent = true)

    internal val installedProvider: MutableList<SPAuthProvider<*, *>> = mutableListOf()

    private var _onComplete: (suspend () -> Unit)? = null
    internal var onCancel: (() -> Unit)? = null
        private set

    private val backends = mutableListOf<SPAuthenticationBackend>()

    internal fun addInstalledProvider(plugin: SPAuthProvider<*, *>) {
        installedProvider.add(plugin)
    }

    init {
        config.install(client = this)
    }

    /**
     * Adds a new authentication backend to handle specific credential types.
     *
     * @param authenticate The backend implementation responsible for
     * verifying user credentials.
     */
    fun addAuthBackend(authenticate: SPAuthenticationBackend) {
        backends.add(authenticate)
    }

    internal fun setOnComplete(callback: suspend () -> Unit) {
        _onComplete = callback
    }

    public fun setOnCancel(callback: () -> Unit) {
        onCancel = callback
    }

    /**
     * Authenticates the provided credential using the appropriate backend.
     * Saves the resulting token to storage and invokes the onComplete callback if defined.
     *
     * @param credential The user's credential used for authentication.
     * @return The authentication token generated upon successful authentication.
     * @throws IllegalStateException if no backend is found to handle the provided
     * credential type.
     */
    suspend fun authenticate(credential: Credential): SPAuthToken {
        val authenticator = backends.firstOrNull { it.type == credential.type }
            ?: error("Credential Provider not found: ${credential.type}")
        val token = authenticator.authenticate(credential)
        config.storage.save(token)
        _onComplete?.invoke()
        return token
    }

    /**
     * Retrieves the user session manager for the specified user type.
     *
     * @return The user session manager responsible for managing the
     * session of the specified user type.
     * @throws IllegalStateException if no session manager is found
     * for the provided user type.
     */
    inline fun <reified TUser : SPAuthUser<*>> userSession(): SPAuthUserSession<TUser> {
        val manager = userSessions[TUser::class]
        return if (manager is SPAuthUserSession<*>) {
            @Suppress("UNCHECKED_CAST")
            manager as SPAuthUserSession<TUser>
        } else {
            throw IllegalStateException("UserManager for ${TUser::class} not found")
        }
    }

    @StartPointDsl
    companion object Plugin : StartPointPlugin<SPAuthConfiguration, SPAuth> {

        private var current: SPAuth? = null

        override val key: AttributeKey<SPAuth>
            get() = AttributeKey("StartPointAuth")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = { navController ->
                current?.let {
                    auth(it, navController)
                } ?: error("${key.name} not installed")
            }

        override fun install(plugin: SPAuth, scope: StartPoint) {
            plugin.setOnComplete {
                withContext(Dispatchers.Main) {
                    scope.navigation.popBackStack(MainContent, inclusive = false)
                }
            }
            current = plugin
        }

        override fun prepare(block: SPAuthConfiguration.() -> Unit, scope: StartPoint): SPAuth {
            return SPAuthConfiguration(scope.context).apply(block).build()
        }

    }
}

/**
 * Retrieves the user session manager for the specified user type as an extension
 * of the StartPoint framework.
 * This allows seamless access to user sessions directly from the StartPoint instance.
 *
 * @return The user session manager responsible for managing the session of the specified user type.
 * @throws IllegalStateException if no session manager is found for the provided user type.
 */
inline fun <reified TUser : SPAuthUser<*>> StartPoint.userSession(): SPAuthUserSession<TUser> {
    return plugin(SPAuth).userSession()
}
