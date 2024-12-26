package com.lavinou.startpoint.auth.passkey

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.passkey.navigation.passkey
import com.lavinou.startpoint.auth.passkey.presentation.PasskeySignUpContent
import com.lavinou.startpoint.dsl.StartPointDsl

class Passkey {

    @Composable
    internal fun SignInContent(
        navHostController: NavHostController,
        startPointAuth: SPAuth
    ) {
        PasskeySignUpContent(
            navHostController = navHostController,
            startPointAuth = startPointAuth
        )
    }

    @Composable
    internal fun SignUpContent(
        navHostController: NavHostController,
        startPointAuth: SPAuth
    ) {
        PasskeySignUpContent(
            navHostController = navHostController,
            startPointAuth = startPointAuth
        )
    }

    @StartPointDsl
    companion object Plugin : SPAuthProvider<PasskeyConfiguration, Passkey> {

        private var currentPlugin: Passkey? = null
        private var currentScope: SPAuth? = null

        override val key: AttributeKey<Passkey>
            get() = AttributeKey("Passkey")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = { navHostController ->
                currentScope?.let {
                    passkey(
                        startPointAuth = it,
                        navHostController = navHostController
                    )
                }
            }

        override fun install(plugin: Passkey, scope: SPAuth) {
            currentPlugin = plugin
            currentScope = scope
        }

        override fun prepare(block: PasskeyConfiguration.() -> Unit): Passkey {
            return PasskeyConfiguration().apply(block).build()
        }

    }

}