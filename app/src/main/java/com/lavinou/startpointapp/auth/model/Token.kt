package com.lavinou.startpointapp.auth.model

import com.lavinou.startpoint.auth.backend.model.SPAuthToken

data class Token(
    override val accessToken: String,
    override val refreshToken: String,
    val user: User?,
    override val expiresAt: Long = System.currentTimeMillis() + 300000
) : SPAuthToken