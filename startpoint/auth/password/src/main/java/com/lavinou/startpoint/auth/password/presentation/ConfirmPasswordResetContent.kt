package com.lavinou.startpoint.auth.password.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lavinou.startpoint.auth.coreui.header.AuthHeader
import com.lavinou.startpoint.auth.coreui.textfield.AuthTextField

@Composable
internal fun ConfirmPasswordResetContent(
    navHostController: NavHostController
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp)
    ) {

        AuthHeader(onBack = {
            navHostController.popBackStack()
        }, title = "Reset Password")

        AuthTextField(label = "Password", value = "", onValueChange = {})
        AuthTextField(label = "Confirm Password", value = "", onValueChange = {})
        Button(onClick = {

        }) {
            Text(text = "Reset")
        }
    }
}