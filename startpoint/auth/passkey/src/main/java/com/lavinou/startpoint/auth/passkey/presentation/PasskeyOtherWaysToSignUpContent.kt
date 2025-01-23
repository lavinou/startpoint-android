package com.lavinou.startpoint.auth.passkey.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lavinou.startpoint.auth.SPAuth
import com.lavinou.startpoint.auth.SPAuthConfiguration
import com.lavinou.startpoint.auth.authPreview
import com.lavinou.startpoint.auth.password.Password
import com.lavinou.startpoint.auth.password.navigation.PasswordSignUp
import com.lavinou.startpoint.auth.phonenumber.PhoneNumber
import com.lavinou.startpoint.auth.providerOrNull

@Composable
internal fun PasskeyOtherWaysToSignUpContent(
    navHostController: NavHostController,
    startPointAuth: SPAuth
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        com.lavinou.startpoint.auth.coreui.header.AuthHeader(
            title = "Other options",
            onBack = {
                navHostController.popBackStack()
            }
        )

        Column {
            startPointAuth.providerOrNull(Password)?.let {
                Button(
                    onClick = {
                        navHostController.navigate(PasswordSignUp)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign up with a password")
                }
            }

            startPointAuth.providerOrNull(PhoneNumber)?.let {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign up with a phone number")
                }
            }
        }

        Column {
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
                        text = "You can sign in securely with your passkey using your fingerprint, face, or other screen-lock method. How passkey work",
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "account-circle",
                        modifier = Modifier.size(48.dp)
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Sign Up")
                }
            }
        }


    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AuthOtherWaysToSignUpPreview() {
    val context = LocalContext.current
    MaterialTheme {
        PasskeyOtherWaysToSignUpContent(
            rememberNavController(),
            startPointAuth = authPreview(context)
        )
    }
}