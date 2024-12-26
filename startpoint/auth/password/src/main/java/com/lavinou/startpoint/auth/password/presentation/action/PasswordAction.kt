package com.lavinou.startpoint.auth.password.presentation.action

import com.lavinou.startpoint.auth.SPAuth

internal sealed interface PasswordAction {

    data class OnViewLoad(
        val loading: Boolean
    ) : PasswordAction

    data class OnPasswordValueChange(
        val value: String
    ) : PasswordAction

    data class OnUsernameValueChange(
        val value: String
    ) : PasswordAction

    data class OnFullNameValueChange(
        val value: String
    ) : PasswordAction

    data class OnSignInSubmit(
        val scope: SPAuth
    ) : PasswordAction

    object ResetForm : PasswordAction
}