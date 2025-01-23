package com.lavinou.startpointapp.auth

import android.content.Context
import com.lavinou.startpoint.StartPointConfiguration
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.biometric.Biometric
import com.lavinou.startpoint.auth.biometric.navigation.BiometricSignIn
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.password.Password
import com.lavinou.startpoint.auth.password.navigation.PasswordSignUp
import com.lavinou.startpoint.auth.storage.DefaultSPAuthStorage
import com.lavinou.startpointapp.R
import com.lavinou.startpointapp.auth.backend.AppUserSessionBackend
import com.lavinou.startpointapp.auth.backend.DefaultBiometricSPAuthBackend
import com.lavinou.startpointapp.auth.backend.DefaultPasswordSPAuthBackend
import com.lavinou.startpointapp.auth.result.resultHandlers
import com.lavinou.startpointapp.auth.validation.passwordValidators
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RefreshToken(
    val access: String
)

fun StartPointConfiguration.installAuth(
    context: Context
) {

    val domain = "192.168.5.23"
    val appStorage = DefaultSPAuthStorage(context)

    val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = appStorage.retrieve()
                    if (token.accessToken.isNullOrBlank()) {
                        return@loadTokens null
                    }

                    BearerTokens(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken
                    )
                }

                refreshTokens {
                    val token = appStorage.retrieve()
                    val response = client.post {
                        headers {
                            header("Content-Type", "application/json")
                        }
                        url("http://$domain:8000/account/token/refresh/")
                        setBody(
                            mapOf(
                                "refresh" to token.refreshToken
                            )
                        )
                    }.body<RefreshToken>()

                    BearerTokens(
                        accessToken = response.access,
                        refreshToken = token.refreshToken
                    )
                }
            }
        }

    }

    val passwordBackend = DefaultPasswordSPAuthBackend(
        client = client,
        baseUrl = "http://$domain:8000"
    )

    val userSessionBackend = AppUserSessionBackend(
        client = client,
        baseUrl = "http://$domain:8000"
    )

    val biometricBackend = DefaultBiometricSPAuthBackend(
        client = client,
        baseUrl = "http://$domain:8000"
    )

    install(SPAuth) {

        title = "Welcome"

        signInButtonRoute = BiometricSignIn
        signUpButtonRoute = PasswordSignUp

        image = R.drawable.crypto_currency_bitcoins_by_vexels

        exitOnUserCancel = false

        setUserSessionBackend(userSessionBackend)

        addProvider(Password) {
            backend = passwordBackend
            passwordValidators()
            resultHandlers()
        }

        addProvider(Biometric) {

            backend = biometricBackend

            image = R.drawable.baseline_fingerprint_24

            onResult = {
                SPAuthNextAction.NavigateTo(PasswordSignUp)
            }
        }
    }
}