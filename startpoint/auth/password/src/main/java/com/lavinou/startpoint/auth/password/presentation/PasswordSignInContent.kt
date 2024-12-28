package com.lavinou.startpoint.auth.password.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.navigation.NavHostController
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.remember.rememberCredentialManager
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.navigation.ForgotPassword
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState
import kotlinx.coroutines.launch

@Composable
internal fun PasswordSignInContent(
    startPointAuth: SPAuth,
    navHostController: NavHostController,
    state: PasswordState,
    onDispatchAction: (PasswordAction) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = rememberCredentialManager()

    LaunchedEffect(key1 = Unit, block = {
        try {
            val credential = credentialManager.getCredential(
                context, request = GetCredentialRequest(
                    listOf(GetPasswordOption())
                )
            ).credential as? PasswordCredential

            credential?.let {
                onDispatchAction(
                    PasswordAction.OnUsernameValueChange(
                        value = credential.id
                    )
                )
                onDispatchAction(
                    PasswordAction.OnPasswordValueChange(
                        value = credential.password
                    )
                )
                onDispatchAction(
                    PasswordAction.OnSignInSubmit(
                        scope = startPointAuth
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("PasswordSignInContent", e.message, e)
        }
    })

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        AuthHeader(
            navHostController = navHostController,
            title = "Sign In"
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    keyboardType = KeyboardType.Email
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "email-icon")
                }
            )

            AuthTextField(
                label = "Password",
                value = state.password,
                onValueChange = {
                    onDispatchAction(
                        PasswordAction.OnPasswordValueChange(
                            value = it
                        )
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "lock-icon")
                }
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "forgot password?",
                    modifier = Modifier.clickable {
                        navHostController.navigate(ForgotPassword)
                    }
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            credentialManager.createCredential(
                                context, request = CreatePasswordRequest(
                                    id = state.email,
                                    password = state.password
                                )
                            )
                        } catch (e: Exception) {
                            Log.e("PasswordSignInContent", e.message, e)
                        }

                        onDispatchAction(
                            PasswordAction.OnSignInSubmit(
                                startPointAuth
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }
            Text(
                text = "Don't have an account? signup",
                modifier = Modifier.clickable {
                    navHostController.navigate(startPointAuth.signUpButtonRoute)
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