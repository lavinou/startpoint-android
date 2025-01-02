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
import com.lavinou.startpoint.auth.navigation.auth
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent
import com.lavinou.startpoint.plugin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SPAuth internal constructor(
    private val config: SPAuthConfiguration
) {
    private val list = mutableListOf<SPAuthenticationBackend>()

    val title: String
        get() = config.title

    val image: Any?
        get() = config.image

    val signUpButtonRoute: Any
        get() = config.signUpButtonRoute

    val signInButtonRoute: Any
        get() = config.signInButtonRoute

    val userSessions = config.userSessions

    val attributes: Attributes = Attributes(concurrent = true)

    val installedProvider: MutableList<SPAuthProvider<*, *>> = mutableListOf()

    private var _onComplete: (suspend () -> Unit)? = null
    val onComplete: (suspend (SPAuthToken) -> Unit)?
        get() {
            return config.onComplete
        }

    fun addInstalledProvider(plugin: SPAuthProvider<*, *>) {
        installedProvider.add(plugin)
    }

    init {
        config.install(client = this)
    }


    fun addAuthBackend(authenticate: SPAuthenticationBackend) {
        list.add(authenticate)
    }

    internal fun setOnComplete(callback: suspend () -> Unit) {
        _onComplete = callback
    }

    suspend fun authenticate(credential: Credential): SPAuthToken {
        val authenticator = list.firstOrNull { it.type == credential.type }
            ?: error("Credential Provider not found: ${credential.type}")
        val token = authenticator.authenticate(credential)
        config.storage.save(token)
        _onComplete?.invoke()
        return token
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

inline fun <reified TUser : SPAuthUser<*>> StartPoint.userSession(): SPAuthUserSession<TUser> {
    return plugin(SPAuth).userSession()
}
