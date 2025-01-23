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
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.navigation.nextActionNavigateTo
import com.lavinou.startpoint.auth.password.model.PasswordValidator
import com.lavinou.startpoint.auth.password.navigation.password
import com.lavinou.startpoint.auth.password.presentation.PasswordSignInContent
import com.lavinou.startpoint.auth.password.presentation.PasswordSignUpContent
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.effect.PasswordEffect
import com.lavinou.startpoint.auth.password.presentation.viewmodel.PasswordViewModel
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent

class Password internal constructor(
    private val backend: PasswordSPAuthBackend,
    private val validators: Map<String, List<PasswordValidator>>,
    private val onResult: ((PasswordResult) -> SPAuthNextAction)? = null
) {

    internal val passwordViewModel = PasswordViewModel(
        backend = backend,
        validators = validators
    )

    @Composable
    internal fun SignInContent(
        navHostController: NavHostController,
        startPointAuth: SPAuth
    ) {

        val state = passwordViewModel.state.collectAsState()
        val effect = passwordViewModel.effect.collectAsState()

        LaunchedEffect(key1 = effect.value, block = {
            val result = effect.value
            result?.let {
                when(val action = onResult?.invoke(result)) {
                    is SPAuthNextAction.NavigateTo -> {
                        passwordViewModel.dispatch(PasswordAction.ResetForm)
                        navHostController.nextActionNavigateTo(action)
                    }
                    is SPAuthNextAction.FieldMessage -> {
                        passwordViewModel.dispatch(PasswordAction.ShowFieldError(
                            field = action.field,
                            message = action.message
                        ))
                    }
                    else -> Unit
                }
            }
        })

        PasswordSignInContent(
            startPointAuth = startPointAuth,
            navHostController = navHostController,
            state = state.value,
            onDispatchAction = passwordViewModel::dispatch,
            isValid = passwordViewModel::isValid
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
            isValid = passwordViewModel::isValid
        )
    }

    @StartPointDsl
    companion object Provider : SPAuthProvider<PasswordConfiguration, Password> {

        public const val PASSWORD_KEY = "password"
        public const val USER_KEY = "email"
        public const val FULL_NAME_KEY = "fullName"

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

        override fun install(provider: Password, scope: SPAuth) {
            currentPlugin = provider
            currentScope = scope
            scope.addAuthBackend(provider.backend)
        }

        override fun prepare(block: PasswordConfiguration.() -> Unit): Password {
            return PasswordConfiguration().apply(block).build()
        }
    }
}