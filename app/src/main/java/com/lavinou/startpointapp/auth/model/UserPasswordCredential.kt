package com.lavinou.startpointapp.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPasswordCredential(
    val username: String,
    val password: String
)

@Serializable
data class UserResponse(
    val pk: Int
)

@Serializable
data class CredentialResponse(
    val refresh: String,
    val access: String,
    val user: UserResponse
)

@Serializable
data class TokenResponse(
    val refresh: String,
    val access: String
)

@Serializable
data class ChallengeResponse(
    val challenge: String
)
