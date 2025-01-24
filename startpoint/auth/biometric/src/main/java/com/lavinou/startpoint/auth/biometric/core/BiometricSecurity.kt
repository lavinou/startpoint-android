package com.lavinou.startpoint.auth.biometric.core

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricPrompt.CryptoObject
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.ECGenParameterSpec

class BiometricSecurity {

    companion object {
        fun generateKeyPair(alias: String): KeyPair? {
            try {
                // Initialize KeyPairGenerator for EC (Elliptic Curve) algorithm
                val keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_EC,  // Algorithm
                    "AndroidKeyStore"               // Keystore
                )

                // Define KeyGenParameterSpec
                val parameterSpec = KeyGenParameterSpec.Builder(
                    alias,  // Key alias
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                )
                    .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))  // Curve
                    .setDigests(KeyProperties.DIGEST_SHA256)  // Digest
                    .setUserAuthenticationRequired(true)    // Require user authentication?
                    .build()

                // Generate the key pair
                keyPairGenerator.initialize(parameterSpec)
                return keyPairGenerator.generateKeyPair()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun getPublicKey(alias: String): String? {
            try {
                // Access the Android Keystore
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)

                // Retrieve the public key
                val publicKey = keyStore.getCertificate(alias)?.publicKey

                // Convert the public key to a Base64-encoded string
                return Base64.encodeToString(publicKey?.encoded, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun signChallenge(
            alias: String,
            challenge: ByteArray,
            cryptoObject: CryptoObject?
        ): String? {
            try {

                // Initialize a Signature instance with the private key
                val signature = cryptoObject?.signature

                // Sign the challenge
                signature?.update(challenge)
                val signedData = signature?.sign()

                // Return the signature as a Base64-encoded string
                return Base64.encodeToString(signedData, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun getCryptoObject(alias: String): CryptoObject? {
            return try {
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)

                // Retrieve the private key
                val privateKey = keyStore.getKey(alias, null) as? PrivateKey
                if (privateKey != null) {
                    val signature = Signature.getInstance("SHA256withECDSA")
                    signature.initSign(privateKey)
                    CryptoObject(signature)
                } else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}