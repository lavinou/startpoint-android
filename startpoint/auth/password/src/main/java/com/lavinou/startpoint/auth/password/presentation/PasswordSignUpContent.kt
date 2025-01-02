package com.lavinou.startpoint.auth.password.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lavinou.startpoint.auth.coreui.remember.rememberCredentialManager
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.Password.Provider.FULL_NAME_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.PASSWORD_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.USER_KEY
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

@Composable
internal fun PasswordSignUpContent(
    navHostController: NavHostController,
    state: PasswordState,
    onDispatchAction: (PasswordAction) -> Unit,
    isValid: (List<String>) -> Boolean
) {

    val context = LocalContext.current
    val credentialManager = rememberCredentialManager()
    val scope = rememberCoroutineScope()
    val softKeyboard = LocalSoftwareKeyboardController.current

    fun validateAndSubmit() {
        if (isValid(listOf(USER_KEY, PASSWORD_KEY, FULL_NAME_KEY))) {
            softKeyboard?.hide()
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        com.lavinou.startpoint.auth.coreui.header.AuthHeader(
            navHostController = navHostController,
            title = "Sign Up"
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
                label = "Full Name",
                value = state.fullName,
                onValueChange = {
                    onDispatchAction(
                        PasswordAction.OnFullNameValueChange(
                            value = it
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "person-icon")
                },
                supportingText = state.errors.fullName?.let { error ->
                    {
                        Text(error)
                    }
                },
                isError = state.errors.fullName != null
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
                keyboardActions = KeyboardActions(
                    onDone = {
                        validateAndSubmit()
                    }
                ),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "lock-icon")
                },
                supportingText = state.errors.password?.let { error ->
                    {
                        Text(error)
                    }
                },
                isError = state.errors.password != null
            )

            Button(
                onClick = {
                    validateAndSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
        }
    }
}