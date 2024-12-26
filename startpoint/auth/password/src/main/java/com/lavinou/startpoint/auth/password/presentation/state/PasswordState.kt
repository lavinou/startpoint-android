package com.lavinou.startpoint.auth.password.presentation.state

internal data class PasswordState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val loading: Boolean = false
)
