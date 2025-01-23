package com.lavinou.startpoint.auth.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.lavinou.startpoint.attribute.AttributeKey
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthProvider
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.biometric.core.BiometricSecurity
import com.lavinou.startpoint.auth.biometric.core.getActivityOrNull
import com.lavinou.startpoint.auth.biometric.navigation.biometricGraph
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.image.AsyncImage
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.navigation.nextActionNavigateTo
import com.lavinou.startpoint.auth.provider
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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

    internal val onCancelRoute: Any? = configuration.onCancelRoute

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(configuration.title)
        .setSubtitle(configuration.subTitle)
        .setAllowedAuthenticators(BIOMETRIC_STRONG)
        .setNegativeButtonText(configuration.cancelText)
        .build()

    private fun createPrompt(
        fragment: FragmentActivity,
        executor: Executor,
        scope: CoroutineScope,
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
        navController: NavController,
        onSuccess: suspend (BiometricIdentifier, String) -> SPAuthToken,
        onCancel: () -> Unit,
        modifier: Modifier = Modifier
    ) {

        val scope = rememberCoroutineScope()
        val manager = BiometricManager.from(fragment)
        val executor = ContextCompat.getMainExecutor(fragment)
        manager.canAuthenticate(BIOMETRIC_STRONG)
        val prompt = createPrompt(
            fragment = fragment,
            executor = executor,
            scope = scope,
            onCancel = onCancel,
            onSuccess = { result ->
                scope.launch {
                    val id = BiometricIdentifier(
                        device = BiometricIdentifier.deviceId(fragment)
                    )
                    val challenge = configuration.backend?.challenge(
                        id = id
                    )
                    val signedChallenge = BiometricSecurity.signChallenge(
                        "test",
                        challenge?.toByteArray() ?: "".toByteArray(),
                        result.cryptoObject
                    )
                    signedChallenge?.let {
                        val token = onSuccess(id, it)
                        val action = configuration.onResult?.invoke(
                            BiometricResult.Success(token)
                        )
                            ?: SPAuthNextAction.NavigateTo(MainContent)
                        when (action) {
                            is SPAuthNextAction.NavigateTo -> {
                                navController.nextActionNavigateTo(action)
                            }

                            else -> Unit
                        }
                    }
                }
            }
        )

        val cryptoObject = BiometricSecurity.getCryptoObject("test")
        cryptoObject?.let {
            prompt.authenticate(promptInfo, cryptoObject)
        }
    }

    @Composable
    internal fun BiometricRegistrationScreen(
        navHostController: NavController,
        modifier: Modifier = Modifier
    ) {

        val fragment = LocalContext.current.getActivityOrNull() as FragmentActivity
        val scope = rememberCoroutineScope()
        val manager = BiometricManager.from(fragment)
        val executor = ContextCompat.getMainExecutor(fragment)

        val prompt = createPrompt(
            fragment = fragment,
            executor = executor,
            scope = scope,
            onCancel = {
                navHostController.popBackStack()
            },
            onSuccess = { result ->
                scope.launch {
                    val key = BiometricSecurity.getPublicKey("test")
                    key?.let {
                        configuration.backend?.register(
                            id = BiometricIdentifier(
                                device = BiometricIdentifier.deviceId(fragment)
                            )
                        )
                    } ?: run {
                        BiometricSecurity.generateKeyPair("test")
                        val publicKey = BiometricSecurity.getPublicKey("test")
                        configuration.backend?.register(
                            id = BiometricIdentifier(
                                device = BiometricIdentifier.deviceId(fragment),
                                publicKey = publicKey
                            )
                        )
                    }
                    val action =
                        configuration.onResult?.invoke(BiometricResult.RegistrationSuccess)
                            ?: SPAuthNextAction.NavigateTo(
                                MainContent
                            )
                    when (action) {
                        is SPAuthNextAction.NavigateTo -> {
                            navHostController.nextActionNavigateTo(action)
                        }

                        else -> Unit
                    }
                }
            }
        )


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                onBack = {
                    navHostController.popBackStack()
                },
                title = "Biometric Registration"
            )

            AsyncImage(
                configuration.image,
                colorFilter = ColorFilter.tint(Color.White)
            )

            Button(
                onClick = {
                    prompt.authenticate(promptInfo)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register Biometric")
            }

        }
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