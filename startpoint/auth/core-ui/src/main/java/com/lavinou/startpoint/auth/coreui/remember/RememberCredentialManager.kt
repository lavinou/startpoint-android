package com.lavinou.startpoint.auth.coreui.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager

@Composable
fun rememberCredentialManager(): CredentialManager {
    val localContext = LocalContext.current
    return remember {
        CredentialManager.create(localContext)
    }
}