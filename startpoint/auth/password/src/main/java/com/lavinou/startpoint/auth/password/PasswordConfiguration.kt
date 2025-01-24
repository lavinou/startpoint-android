package com.lavinou.startpoint.auth.password

import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import com.lavinou.startpoint.auth.navigation.SPAuthNextAction
import com.lavinou.startpoint.auth.password.model.PasswordValidator
import com.lavinou.startpoint.dsl.StartPointDsl
import com.lavinou.startpoint.navigation.MainContent

@StartPointDsl
public class PasswordConfiguration {

    var onResult: ((PasswordResult) -> SPAuthNextAction)? = null

    var backend: PasswordSPAuthBackend? = null

    private val validators = mutableMapOf<String, MutableList<PasswordValidator>>()

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

        val backend = backend ?: error("Password Auth Backend not set")

        return Password(
            backend = backend,
            validators = validators,
            onResult = onResult
        )
    }
}