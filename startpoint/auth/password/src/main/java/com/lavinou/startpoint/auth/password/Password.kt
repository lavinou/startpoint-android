package com.lavinou.startpoint.auth.password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.password.navigation.password
import com.lavinou.startpoint.auth.password.presentation.PasswordSignInContent
import com.lavinou.startpoint.auth.password.presentation.PasswordSignUpContent
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.effect.PasswordEffect
import com.lavinou.startpoint.auth.password.presentation.viewmodel.PasswordViewModel
import com.lavinou.startpoint.dsl.StartPointDsl

class Password(
    val backend: PasswordSPAuthBackend
) {

    private val passwordViewModel = PasswordViewModel(
        backend = backend
    )

    private var _onSuccess: (suspend (SPAuthToken) -> Unit)? = null

    @Composable
    internal fun SignInContent(
        navHostController: NavHostController,
        startPointAuth: SPAuth
    ) {

        val state = passwordViewModel.state.collectAsState()
        val effect = passwordViewModel.effect.collectAsState()

        LaunchedEffect(key1 = effect.value, block = {
            val effect = effect.value
            when (effect) {
                is PasswordEffect.OnSuccess -> {
                    _onSuccess?.invoke(effect.token)
                    passwordViewModel.dispatch(PasswordAction.ResetForm)

                }

                else -> Unit
            }
        })

        PasswordSignInContent(
            startPointAuth = startPointAuth,
            navHostController = navHostController,
            state = state.value,
            onDispatchAction = passwordViewModel::dispatch
        )
    }

    @Composable
    internal fun SignUpContent(
        navHostController: NavHostController
    ) {
        val state = passwordViewModel.state.collectAsState()


        PasswordSignUpContent(
            navHostController = navHostController,
            state = state.value,
            onDispatchAction = passwordViewModel::dispatch,
        )
    }

    internal fun onSuccess(callback: suspend (SPAuthToken) -> Unit) {
        _onSuccess = callback
    }

    @StartPointDsl
    companion object Provider : SPAuthProvider<PasswordConfiguration, Password> {

        private var currentPlugin: Password? = null
        private var currentScope: SPAuth? = null

        override val key: AttributeKey<Password>
            get() = AttributeKey("Password")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = { navHostController ->
                currentScope?.let {
                    password(
                        startPointAuth = it,
                        navHostController = navHostController
                    )
                }
            }

        override fun install(plugin: Password, scope: SPAuth) {
            currentPlugin = plugin
            currentScope = scope
            scope.addAuthBackend(plugin.backend)
            plugin.onSuccess { value ->
                scope.onComplete?.invoke(value)
            }

        }

        override fun prepare(block: PasswordConfiguration.() -> Unit): Password {
            return PasswordConfiguration().apply(block).build()
        }

    }

}