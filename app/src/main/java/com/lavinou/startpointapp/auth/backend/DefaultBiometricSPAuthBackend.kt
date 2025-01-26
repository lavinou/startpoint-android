package com.lavinou.startpointapp.auth.backend

import android.util.Log
import androidx.credentials.Credential
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.biometric.BiometricIdentifier
import com.lavinou.startpoint.auth.biometric.BiometricSPAuthBackend
import com.lavinou.startpoint.auth.biometric.credentials.BiometricCredential
import com.lavinou.startpointapp.auth.model.ChallengeResponse
import com.lavinou.startpointapp.auth.model.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class DefaultBiometricSPAuthBackend(
    private val client: HttpClient,
    private val baseUrl: String
) : BiometricSPAuthBackend {

    private suspend fun verify(
        id: BiometricIdentifier,
        signedChallenge: String
    ): SPAuthToken {

        val token = client.post {
            url("$baseUrl/account/biometric/token/")
            headers {
                header("Content-Type", "application/json")
            }
            setBody(
                mapOf(
                    "device_id" to id.device,
                    "signed_challenge" to signedChallenge
                )
            )
        }.body<TokenResponse>()

        return object : SPAuthToken {
            override val accessToken: String
                get() = token.access

            override val refreshToken: String
                get() = token.refresh

            override val expiresAt: Long
                get() = System.currentTimeMillis() + 300000
        }
    }

    override suspend fun authenticate(credential: Credential): SPAuthToken {
        val biometricCredential = credential as BiometricCredential
        val token = verify(
            id = biometricCredential.id,
            signedChallenge = biometricCredential.signedChallenge
        )
        return token
    }

    override suspend fun register(id: BiometricIdentifier): Boolean {
        val response = client.post {
            url("$baseUrl/account/biometric/register/")
            headers {
                header("Content-Type", "application/json")
            }
            setBody(
                mapOf(
                    "device_id" to id.device,
                    "public_key" to id.publicKey
                )
            )
        }
        Log.d("BiometricSPAuthBackend", response.bodyAsText())
        return response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK
    }

    override suspend fun unregister(id: BiometricIdentifier): Boolean {
        val response = client.post {
            url("$baseUrl/account/biometric/unregister/")
            headers {
                header("Content-Type", "application/json")
            }
            setBody(
                mapOf(
                    "device_id" to id.device
                )
            )
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun challenge(id: BiometricIdentifier): String {
        val response: ChallengeResponse = client.post {
            url("$baseUrl/account/biometric/challenge/")
            headers {
                header("Content-Type", "application/json")
            }
            setBody(
                mapOf(
                    "device_id" to id.device
                )
            )
        }.body()

        return response.challenge
    }
}