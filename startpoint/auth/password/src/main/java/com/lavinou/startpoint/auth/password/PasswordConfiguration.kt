package com.lavinou.startpoint.auth.password

import com.lavinou.startpoint.auth.password.model.PasswordValidator
import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
public class PasswordConfiguration {

    private var _backend: PasswordSPAuthBackend? = null

    private val validators = mutableMapOf<String, MutableList<PasswordValidator>>()

    public fun setBackend(backend: PasswordSPAuthBackend) {
        _backend = backend
    }

    public fun addValidator(key: String, rule: (String) -> Boolean, message: String) {
        val validator = PasswordValidator(
            rule = rule,
            message = message
        )
        validators[key]?.add(validator) ?: kotlin.run {
            validators[key] = mutableListOf(validator)
        }
    }

    internal fun build(): Password {

        val backend = _backend ?: error("Password Auth Backend not set")

        return Password(
            backend = backend,
            validators = validators
        )
    }
}