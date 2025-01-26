package com.lavinou.startpoint.auth.biometric

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

data class BiometricIdentifier(
    val device: String,
    val publicKey: String? = null
) {

    companion object {

        @SuppressLint("HardwareIds")
        fun deviceId(from: Context): String {
            return Settings.Secure.getString(from.contentResolver,
                Settings.Secure.ANDROID_ID)
        }
    }
}
