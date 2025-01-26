package com.lavinou.startpointapp.auth.backend

import com.lavinou.startpoint.auth.backend.SPUserSessionBackend
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpointapp.auth.model.AppUser
import com.lavinou.startpointapp.auth.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode

class AppUserSessionBackend(
    private val client: HttpClient,
    private val baseUrl: String
) : SPUserSessionBackend<AppUser> {

    override suspend fun logout(token: SPAuthToken): Boolean {
        val response = client.post {
            headers {
                header("Content-Type", "application/json")
            }
            url("$baseUrl/account/logout/")
        }
        return true
    }

    override suspend fun user(token: SPAuthToken?): AppUser {
        return if (!token?.accessToken.isNullOrBlank()) {
            val response = client.get {
                url("$baseUrl/account/user/")
                headers {
                    header("Content-Type", "application/json")
                    header("Authorization", "Bearer ${token?.accessToken}")
                }
            }
            if(response.status == HttpStatusCode.OK) {
                val user: User = response.body()
                AppUser.AuthenticatedUser(id = "${user.pk}", value = user)
            }
            else
                AppUser.AnonymousUser()
        } else {
            AppUser.AnonymousUser()
        }
    }
}