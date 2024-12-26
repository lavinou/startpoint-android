package com.lavinou.startpoint.auth.passkey

import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class PasskeyConfiguration {

    fun build(): Passkey {
        return Passkey()
    }
}