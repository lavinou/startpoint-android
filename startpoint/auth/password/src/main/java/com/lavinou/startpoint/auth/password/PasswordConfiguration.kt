package com.lavinou.startpoint.auth.password

import com.lavinou.startpoint.auth.password.model.PasswordValidator
import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class PasswordConfiguration {

    private var _backend: PasswordSPAuthBackend? = null

    internal val validators = mutableMapOf<String, MutableList<PasswordValidator>>()

    fun setBackend(backend: PasswordSPAuthBackend) {
        _backend = backend
    }

    fun addValidator(key: String, rule: (String) -> Boolean, message: String) {
        val validator = PasswordValidator(
            rule = rule,
            message = message
        )
        validators[key]?.add(validator) ?: kotlin.run {
            validators[key] = mutableListOf(validator)
        }
    }

    fun build(): Password {

        val backend = _backend ?: error("Password Auth Backend not set")

        return Password(
            backend = backend,
            validators = validators
        )
    }
}