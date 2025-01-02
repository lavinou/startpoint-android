package com.lavinou.startpoint.auth.password.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.Password.Provider.USER_KEY
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

@Composable
internal fun ForgotPasswordContent(
    navHostController: NavHostController,
    state: PasswordState,
    onDispatchAction: (PasswordAction) -> Unit,
    isValid: (List<String>) -> Boolean
) {

    fun validateAndSubmit() {
        if (isValid(listOf(USER_KEY))) {
            onDispatchAction(PasswordAction.OnPasswordReset)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onDispatchAction(PasswordAction.ResetForm)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        AuthHeader(
            navHostController = navHostController,
            title = "Let get you back in!"
        )

        AuthTextField(
            label = "Email",
            value = state.email,
            onValueChange = {
                onDispatchAction(
                    PasswordAction.OnUsernameValueChange(
                        value = it
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    validateAndSubmit()
                }
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "email-icon")
            },
            supportingText = state.errors.email?.let { error ->
                {
                    Text(error)
                }
            },
            isError = state.errors.email != null
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                validateAndSubmit()
            }) {
                Text(text = "Lets Go")
            }
        }

    }
}