package com.lavinou.startpoint.auth.password.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField
import com.lavinou.startpoint.auth.password.presentation.action.PasswordAction
import com.lavinou.startpoint.auth.password.presentation.state.PasswordState

@Composable
fun FitnessLoginContent(
    state: PasswordState,
    onBack: () -> Unit,
    onDispatch: (PasswordAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Column {
            Image(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "back arrow"
            )

            Text(
                text = "Sign In",
                style = MaterialTheme.typography.titleLarge
            )

            AuthTextField(
                label = "Email",
                value = "",
                onValueChange = {}
            )

            AuthTextField(
                label = "Password",
                value = "",
                onValueChange = {}
            )
        }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {}
            ) {
                Text("Sign Up")
            }

            IconButton(
                onClick = {},

            ) {
                Image(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = "continue"
                )
            }
        }


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FitnessLoginContent_Preview() {
    MaterialTheme {
        FitnessLoginContent(
            state = PasswordState(),
            onBack = {},
            onDispatch = {}
        )
    }
}