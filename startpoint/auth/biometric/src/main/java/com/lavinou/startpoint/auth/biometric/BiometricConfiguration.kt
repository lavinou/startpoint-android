package com.lavinou.startpoint.auth.biometric

import com.lavinou.startpoint.auth.navigation.SPAuthNextAction

class BiometricConfiguration {

    var title: String = "Login with Biometrics"

    var subTitle: String = "Log in using your biometric credential"

    var cancelText: String = "Use account password"

    var backend: BiometricSPAuthBackend? = null

    var onCancelRoute: Any? = null

    var onResult: ((BiometricResult) -> SPAuthNextAction)? = null

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