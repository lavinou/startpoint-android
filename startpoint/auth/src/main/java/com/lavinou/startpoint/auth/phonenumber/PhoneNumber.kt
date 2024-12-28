package com.lavinou.startpoint.auth.phonenumber

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.dsl.StartPointDsl

class PhoneNumber {

//    init {
//
//    }
//
//    @Composable
//    internal fun SignInContent(
//        navHostController: NavHostController
//    ) {
//        val vm: com.startpoint.auth.password.presentation.viewmodel.PasswordViewModel = koinViewModel()
//        val state = vm.state.collectAsState()
//        com.startpoint.auth.password.presentation.PasswordSignInContent(
//            navHostController = navHostController,
//            state = state.value,
//            url = URL("https://google.com"),
//            onDispatchAction = vm::dispatch
//        )
//    }
//
//    @Composable
//    internal fun SignUpContent(
//        navHostController: NavHostController
//    ) {
//        val vm: com.startpoint.auth.password.presentation.viewmodel.PasswordViewModel = koinViewModel()
//        val state = vm.state.collectAsState()
//        com.startpoint.auth.password.presentation.PasswordSignUpContent(
//            navHostController = navHostController,
//            state = state.value,
//            onDispatchAction = vm::dispatch
//        )
//    }

    @StartPointDsl
    companion object Plugin : SPAuthProvider<PhoneNumberConfiguration, PhoneNumber> {

        private var currentPlugin: PhoneNumber? = null
        private var currentScope: SPAuth? = null

        override val key: AttributeKey<PhoneNumber>
            get() = AttributeKey("PhoneNumber")

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = { navHostController ->
                currentScope?.let {
//                    password(
//                        startPointAuth = it,
//                        navHostController = navHostController
//                    )
                }
            }

        override fun install(plugin: PhoneNumber, scope: SPAuth) {
            currentPlugin = plugin
            currentScope = scope
        }

        override fun prepare(block: PhoneNumberConfiguration.() -> Unit): PhoneNumber {
            return PhoneNumberConfiguration().apply(block).build()
        }

    }

}