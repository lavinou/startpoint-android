package com.lavinou.startpoint.auth.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.auth.biometric.core.BiometricSecurity
import com.lavinou.startpoint.auth.biometric.navigation.biometricGraph
import com.lavinou.startpoint.auth.biometric.presentation.BiometricViewModel
import com.lavinou.startpoint.auth.biometric.presentation.action.BiometricAction
import com.lavinou.startpoint.auth.biometric.presentation.component.BiometricRegistrationContent
import com.lavinou.startpoint.auth.biometric.presentation.effect.BiometricEffect
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.provider
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent
import java.util.concurrent.Executor

/**
 * Biometric is used with SPAuthToken. When a user logs out auth token is cleared
 * however when biometric was registered we kept the refresh token. When a user decides
 * to authenticate again we call the refresh token. if the token is still good, then
 * we get the latest access token.
 */
class Biometric(
    private val configuration: BiometricConfiguration
) {

    internal val viewModel = BiometricViewModel(
        backend = configuration.backend ?: error("Biometric backend cannot be null"),
        security = BiometricSecurity()
    )

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(configuration.title)
        .setSubtitle(configuration.subTitle)
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setNegativeButtonText(configuration.cancelText)
        .build()

    private fun createPrompt(
        fragment: FragmentActivity,
        executor: Executor,
        onCancel: () -> Unit,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {
        return BiometricPrompt(
            fragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        BiometricPrompt.ERROR_LOCKOUT,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                        BiometricPrompt.ERROR_CANCELED -> {
                            onCancel()
                        }
                    }
                }
            }
        )
    }

    @Composable
    internal fun BiometricPromptDialog(
        fragment: FragmentActivity,
        onNextAction: (SPAuthNextAction) -> Unit,
        onBiometricResult: (BiometricPrompt.AuthenticationResult) -> Unit,
    ) {

        val manager = BiometricManager.from(fragment)
        val executor = ContextCompat.getMainExecutor(fragment)
        manager.canAuthenticate(BIOMETRIC_STRONG)

        val prompt = createPrompt(
            fragment = fragment,
            executor = executor,
            onCancel = {
                val action = configuration.onResult?.invoke(BiometricResult.OnUserCancelled)
                    ?: SPAuthNextAction.NavigateTo(MainContent)
                onNextAction(action)
            },
            onSuccess = onBiometricResult
        )
        val effect by viewModel.effect.collectAsState()

        LaunchedEffect(Unit) {
            val cryptoObject = BiometricSecurity.getCryptoObject("test")
            cryptoObject?.let {
                prompt.authenticate(promptInfo, cryptoObject)
            } ?: run {
                val action = configuration.onResult?.invoke(BiometricResult.BiometricNotRegistered)
                    ?: SPAuthNextAction.NavigateTo(MainContent)
                onNextAction(action)
            }
        }

        LaunchedEffect(effect) {
            when (effect) {
                is BiometricEffect.Success -> {
                    val token = (effect as BiometricEffect.Success).token
                    val action = configuration.onResult?.invoke(BiometricResult.Success(token))
                        ?: SPAuthNextAction.NavigateTo(MainContent)

                    onNextAction(action)
                    viewModel.dispatch(BiometricAction.Reset)
                }

                else -> Unit
            }
        }

    }

    @Composable
    internal fun BiometricRegistrationScreen(
        fragment: FragmentActivity,
        onCancel: () -> Unit,
        onNavigate: (SPAuthNextAction.NavigateTo) -> Unit,
        modifier: Modifier = Modifier
    ) {

        val executor = ContextCompat.getMainExecutor(fragment)

        val prompt = createPrompt(
            fragment = fragment,
            executor = executor,
            onCancel = onCancel,
            onSuccess = { result ->
                viewModel.dispatch(
                    BiometricAction.Register(
                        result = result,
                        deviceId = BiometricIdentifier.deviceId(fragment)
                    )
                )
            }
        )

        val effect by viewModel.effect.collectAsState()

        LaunchedEffect(effect) {
            when (effect) {
                is BiometricEffect.RegistrationSuccess -> {
                    val action = configuration.onResult?.invoke(BiometricResult.RegistrationSuccess)
                        ?: SPAuthNextAction.NavigateTo(MainContent)
                    when (action) {
                        is SPAuthNextAction.NavigateTo -> onNavigate(action)
                        else -> Unit
                    }
                    viewModel.dispatch(BiometricAction.Reset)
                }

                else -> Unit
            }
        }

        BiometricRegistrationContent(
            onCancel = onCancel,
            onRegisterClick = {
                prompt.authenticate(promptInfo)
            },
            image = configuration.image,
            modifier = modifier
        )
    }

    @StartPointDsl
    companion object Provide : SPAuthProvider<BiometricConfiguration, Biometric> {

        private var currentScope: SPAuth? = null

        override val graph: NavGraphBuilder.(NavHostController) -> Unit
            get() = { navController ->
                currentScope?.let {
                    biometricGraph(it, navController)
                }
            }

        override val key: AttributeKey<Biometric>
            get() = AttributeKey("Biometric")

        override fun prepare(block: BiometricConfiguration.() -> Unit): Biometric {
            return BiometricConfiguration().apply(block).build()
        }

        override fun install(provider: Biometric, scope: SPAuth) {
            currentScope = scope
            val backend = provider.configuration.backend ?: error("Biometric backend not installed")
            scope.addAuthBackend(backend)
        }
    }
}

val SPAuth.biometric: Biometric
    get() = provider(Biometric)