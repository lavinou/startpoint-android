package com.lavinou.startpoint.auth.password.presentation.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

@Composable
fun DefaultLoginContent(
    state: PasswordState,
    onBack: () -> Unit,
    onDispatch: (PasswordAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        AuthHeader(
            onBack = onBack,
            title = "Sign In"
        )

        state.errors.server?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthTextField(
                label = "Email",
                value = state.email,
                onValueChange = {
                    onDispatch(
                        PasswordAction.OnUsernameValueChange(
                            value = it
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
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

            AuthTextField(
                label = "Password",
                value = state.password,
                onValueChange = {
                    onDispatch(
                        PasswordAction.OnPasswordValueChange(
                            value = it
                        )
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "lock-icon")
                },
                supportingText = state.errors.password?.let { error ->
                    {
                        Text(error)
                    }
                },
                isError = state.errors.password != null,
                keyboardActions = KeyboardActions(
                    onDone = {
//                            validateAndSubmit()
                    }
                )
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "forgot password?",
                    modifier = Modifier.clickable {
//                            navHostController.navigate(ForgotPassword)
                    },
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Button(
                onClick = {
//                        validateAndSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
            Text(
                text = "Don't have an account? signup",
                modifier = Modifier.clickable {
//                        navHostController.navigate(startPointAuth.signUpButtonRoute)
                },
                color = MaterialTheme.colorScheme.secondary
            )

            if (state.loading)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginContent_Preview() {
    MaterialTheme {
        DefaultLoginContent(
            state = PasswordState(),
            onBack = {},
            onDispatch = {}
        )
    }
}