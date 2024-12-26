package com.lavinou.startpoint.auth.password

import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class PasswordConfiguration {

    private var _backend: PasswordSPAuthBackend? = null

    fun setBackend(backend: PasswordSPAuthBackend) {
        _backend = backend
    }

    fun build(): Password {

        val backend = _backend ?: error("Password Auth Backend not set")

        return Password(
            backend = backend
        )
    }
}