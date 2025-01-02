package com.lavinou.startpoint.auth.password.model

data class PasswordValidator(
    val rule: (String) -> Boolean,
    val message: String
)
