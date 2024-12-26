package com.lavinou.startpoint.auth.backend.model

interface SPAuthToken {

    val accessToken: String

    val refreshToken: String

    val expiresAt: Long

    companion object {
        val Default: SPAuthToken = object : SPAuthToken {
            override val accessToken: String
                get() = ""

            override val refreshToken: String
                get() = ""

            override val expiresAt: Long
                get() = -1
        }
    }
}