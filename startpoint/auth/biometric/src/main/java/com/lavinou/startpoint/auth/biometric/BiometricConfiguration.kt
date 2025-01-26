package com.lavinou.startpoint.auth.biometric

import com.lavinou.startpoint.auth.navigation.SPAuthNextAction

class BiometricConfiguration {

    public var title: String = "Login with Biometrics"

    public var subTitle: String = "Log in using your biometric credential"

    public var cancelText: String = "Use account password"

    public var backend: BiometricSPAuthBackend? = null

    public var onResult: ((BiometricResult) -> SPAuthNextAction)? = null

    /**
     * An optional image associated with the biometric authentication process.
     */
    public var image: Any? = null


    internal fun build(): Biometric {
        return Biometric(
            configuration = this
        )
    }
}