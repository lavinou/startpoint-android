package com.lavinou.startpoint.auth.password.presentation.viewmodel

import androidx.credentials.PasswordCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavinou.startpoint.auth.password.Password.Provider.PASSWORD_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.USER_KEY
import com.lavinou.startpoint.auth.password.PasswordSPAuthBackend
import com.lavinou.startpoint.auth.password.model.PasswordValidator
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.effect.PasswordEffect
import com.lavinou.startpoint.auth.password.presentation.state.PasswordErrorState
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class PasswordViewModel(
    private val validators: Map<String, List<PasswordValidator>> = emptyMap()
) : ViewModel() {

    private val _effect = MutableStateFlow<PasswordEffect?>(null)
    val effect: StateFlow<PasswordEffect?>
        get() = _effect

    private val _state = MutableStateFlow(PasswordState())
    val state: StateFlow<PasswordState>
        get() = _state

    fun dispatch(action: PasswordAction) {
        when (action) {
            is PasswordAction.OnPasswordValueChange -> {
                _state.update {
                    it.copy(
                        password = action.value
                    )
                }

                _state.value.errors.password?.let {
                    isValid()
                }
            }

            is PasswordAction.OnUsernameValueChange -> {
                _state.update {
                    it.copy(
                        email = action.value
                    )
                }
                _state.value.errors.email?.let {
                    isValid()
                }
            }

            is PasswordAction.OnFullNameValueChange -> {
                _state.update {
                    it.copy(
                        fullName = action.value
                    )
                }
                _state.value.errors.fullName?.let {
                    isValid()
                }
            }

            is PasswordAction.OnViewLoad -> {
                _state.update {
                    it.copy(loading = action.loading)
                }
            }

            is PasswordAction.OnSignInSubmit -> {
                viewModelScope.launch {
                    dispatch(PasswordAction.OnViewLoad(true))
                    val token = action.scope.authenticate(
                        PasswordCredential(
                            id = state.value.email,
                            password = state.value.password
                        )
                    )
                    dispatch(PasswordAction.OnViewLoad(false))
                    _effect.update {
                        PasswordEffect.OnSuccess(token)
                    }
                }
            }

            PasswordAction.ResetForm -> {
                _effect.update {
                    null
                }
                _state.update {
                    PasswordState()
                }
            }
        }
    }

    fun isValid(): Boolean {

        var anyErrors = false
        _state.update {
            it.copy(errors = PasswordErrorState())
        }

        validators[USER_KEY]?.map { validate ->
            if (validate.rule(state.value.email)) {
                _state.update {
                    it.copy(
                        errors = it.errors.copy(
                            email = validate.message
                        )
                    )
                }
                anyErrors = true
                return@map
            }
        }

        validators[PASSWORD_KEY]?.map { validate ->
            if (validate.rule(state.value.password)) {
                _state.update {
                    it.copy(
                        errors = it.errors.copy(
                            password = validate.message
                        )
                    )
                }
                anyErrors = true
            }

            return@map
        }

        return anyErrors.not()
    }

}