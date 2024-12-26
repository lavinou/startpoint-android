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
import com.lavinou.startpoint.plugin

class SPAuth(
    val title: String,
    val signInButtonRoute: Any = Any(),
    val signUpButtonRoute: Any = Any(),
    val config: SPAuthConfiguration
) {
    private val list = mutableListOf<SPAuthenticationBackend>()

    val attributes: Attributes = Attributes(concurrent = true)

    val installedProvider: MutableList<SPAuthProvider<*, *>> = mutableListOf()

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

    suspend fun authenticate(credential: Credential): SPAuthToken {
        val authenticator = list.firstOrNull { it.type == credential.type }
            ?: error("Credential Provider not found: ${credential.type}")
        val token = authenticator.authenticate(credential)
        config.storage?.save(token)
        return token
    }

    inline fun <reified TUser : SPAuthUser<*>> userSession(): SPAuthUserSession<TUser> {
        return config.userSession()
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
            current = plugin
        }

        override fun prepare(block: SPAuthConfiguration.() -> Unit): SPAuth {
            return SPAuthConfiguration().apply(block).build()
        }

    }
}

inline fun <reified TUser : SPAuthUser<*>> StartPoint.userSession(): SPAuthUserSession<TUser> {
    return plugin(SPAuth).userSession()
}
