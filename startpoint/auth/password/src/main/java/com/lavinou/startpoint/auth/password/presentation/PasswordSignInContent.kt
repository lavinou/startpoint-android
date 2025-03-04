package com.lavinou.startpoint.auth.password.presentation

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.navigation.NavHostController
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.remember.rememberCredentialManager
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.Password.Provider.PASSWORD_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.USER_KEY
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
    isValid: (List<String>) -> Boolean
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = rememberCredentialManager()
    val softKeyboard = LocalSoftwareKeyboardController.current

    fun submit() {
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
            softKeyboard?.hide()
        }
    }

    fun validateAndSubmit() {
        if (isValid(listOf(USER_KEY, PASSWORD_KEY))) {
            submit()
        }
    }

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
            onBack = {
                navHostController.popBackStack()
            },
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
                    onDispatchAction(
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
                    onDispatchAction(
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
                        validateAndSubmit()
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
                        navHostController.navigate(ForgotPassword)
                    },
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Button(
                onClick = {
                    validateAndSubmit()
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
