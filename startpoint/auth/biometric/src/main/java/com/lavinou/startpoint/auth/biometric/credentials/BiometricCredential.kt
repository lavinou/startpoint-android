package com.lavinou.startpoint.auth.biometric.credentials

import android.os.Bundle
import androidx.credentials.CustomCredential
import com.lavinou.startpoint.auth.biometric.BiometricIdentifier

class BiometricCredential private constructor(
    val id: BiometricIdentifier,
    val signedChallenge: String,
    data: Bundle,
) : CustomCredential(
    type = TYPE,
    data = data
) {

    constructor(
        id: BiometricIdentifier,
        signedChallenge: String
    ) : this(id, signedChallenge, toBundle(id, signedChallenge))

    init {
        require(id.device.isNotEmpty()) { "deviceId is required" }
        require(signedChallenge.isNotEmpty()) { "signedChallenge is required" }
    }

    companion object {

        private const val PACKAGE = "com.lavinou.startpoint.auth.biometric.credentials"

        const val TYPE = "$PACKAGE.BiometricCredential"

        internal const val BUNDLE_KEY_DEVICE_ID = "$PACKAGE.BUNDLE_KEY_DEVICE_ID"
        internal const val BUNDLE_KEY_SIGNED_CHALLENGE = "$PACKAGE.BUNDLE_KEY_SIGNED_CHALLENGE"

        internal fun toBundle(id: BiometricIdentifier, signedChallenge: String): Bundle {
            val bundle = Bundle()
            bundle.putString(BUNDLE_KEY_DEVICE_ID, id.device)
            bundle.putString(BUNDLE_KEY_SIGNED_CHALLENGE, signedChallenge)
            return bundle
        }

        internal fun createFrom(data: Bundle): BiometricCredential {
            val deviceId = data.getString(BUNDLE_KEY_DEVICE_ID)!!
            val signedChallenge = data.getString(BUNDLE_KEY_SIGNED_CHALLENGE)!!
            return BiometricCredential(
                id = BiometricIdentifier(deviceId),
                signedChallenge = signedChallenge
            )
        }
    }
}