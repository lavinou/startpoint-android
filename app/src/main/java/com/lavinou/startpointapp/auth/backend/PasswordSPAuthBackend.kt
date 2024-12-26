package com.lavinou.startpointapp.auth.backend

import androidx.credentials.Credential
import androidx.credentials.PasswordCredential
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.password.PasswordSPAuthBackend
import com.lavinou.startpointapp.auth.model.CredentialResponse
import com.lavinou.startpointapp.auth.model.Token
import com.lavinou.startpointapp.auth.model.User
import com.lavinou.startpointapp.auth.model.UserPasswordCredential
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url

class DefaultPasswordSPAuthBackend(
    private val client: HttpClient,
    private val baseUrl: String
) : PasswordSPAuthBackend {

    override suspend fun authenticate(credential: Credential): SPAuthToken {
        if (PasswordCredential.TYPE_PASSWORD_CREDENTIAL != credential.type)
            return SPAuthToken.Default

        val passwordCredential = credential as PasswordCredential

        val response: CredentialResponse = client.post {
            headers {
                header("Content-Type", "application/json")
            }
            url("$baseUrl/account/login/")
            setBody(
                UserPasswordCredential(
                    username = passwordCredential.id,
                    password = passwordCredential.password
                )
            )
        }.body()

        return Token(
            accessToken = response.access,
            refreshToken = response.refresh,
            user = User(
                pk = response.user.pk,
                username = ""
            )
        )
    }

    override suspend fun resetPassword(email: String): Boolean {
        return false
    }

    override suspend fun confirmPasswordReset(): Boolean {
        return false
    }

    override suspend fun changePassword(): Boolean {
        return false
    }
}