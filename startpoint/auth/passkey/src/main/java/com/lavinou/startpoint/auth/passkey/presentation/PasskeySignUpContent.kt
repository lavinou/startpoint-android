package com.lavinou.startpoint.auth.passkey.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthConfiguration
import com.lavinou.startpoint.auth.containsAnyBut
import com.lavinou.startpoint.auth.passkey.Passkey
import com.lavinou.startpoint.auth.passkey.navigation.PasskeyOtherWaysToSignUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PasskeySignUpContent(
    navHostController: NavHostController,
    startPointAuth: SPAuth
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back-arrow",
                modifier = Modifier.clickable {
                    navHostController.popBackStack()
                }
            )

            Icon(imageVector = Icons.Default.Face, contentDescription = "login")
        }

        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineLarge
        )

        Column {
            Text(
                text = "Full Name",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = "",
                onValueChange = {

                },
                shape = MaterialTheme.shapes.extraLarge,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "email-icon")
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Email",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = "",
                onValueChange = {

                },
                shape = MaterialTheme.shapes.extraLarge,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "email-icon")
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
        }

        Column {
            Text(
                text = "Signing In",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.large
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "A passkey is a faster and safer way to sign in than a password. Your account is created with one unless you use another option. How Passkey work",
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "account-circle",
                        modifier = Modifier.size(48.dp)
                    )
                }
                if (startPointAuth.containsAnyBut(Passkey)) {
                    Text(
                        text = "Other ways to sign in",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                                navHostController.navigate(PasskeyOtherWaysToSignUp)
                            }
                    )
                }
            }
        }

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "Sign Up")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AuthSignUpScreenPreview() {
    MaterialTheme {
        PasskeySignUpContent(
            rememberNavController(),
            startPointAuth = SPAuth(
                title = "",
                config = SPAuthConfiguration()
            )
        )
    }
}