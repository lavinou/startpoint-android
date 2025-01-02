package com.lavinou.startpoint.auth.password.presentation.state

internal data class PasswordErrorState(
    val email: String? = null,
    val password: String? = null,
    val fullName: String? = null,
    val server: String? = null
)

internal data class PasswordState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val errors: PasswordErrorState = PasswordErrorState(),
    val loading: Boolean = false
)
