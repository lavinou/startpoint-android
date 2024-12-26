package com.lavinou.startpoint.auth.password.presentation.viewmodel

import androidx.credentials.PasswordCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavinou.startpoint.auth.password.PasswordSPAuthBackend
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.effect.PasswordEffect
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class PasswordViewModel(
    private val backend: PasswordSPAuthBackend
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
            }

            is PasswordAction.OnUsernameValueChange -> {
                _state.update {
                    it.copy(
                        email = action.value
                    )
                }
            }

            is PasswordAction.OnFullNameValueChange -> {
                _state.update {
                    it.copy(
                        fullName = action.value
                    )
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

}