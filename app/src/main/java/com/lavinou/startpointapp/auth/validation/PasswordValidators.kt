package com.lavinou.startpointapp.auth.validation

import com.lavinou.startpoint.auth.password.Password.Provider.FULL_NAME_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.PASSWORD_KEY
import com.lavinou.startpoint.auth.password.Password.Provider.USER_KEY
import com.lavinou.startpoint.auth.password.PasswordConfiguration

fun PasswordConfiguration.passwordValidators() {
    addValidator(
        key = USER_KEY,
        rule = { value -> value.isBlank() },
        message = "make sure to enter an email address"
    )
    addValidator(
        key = PASSWORD_KEY,
        rule = { value -> value.isBlank() },
        message = "Please add your password"
    )
    addValidator(
        key = PASSWORD_KEY,
        rule = { value -> value.length in 1..7 },
        message = "password is too short."
    )

    addValidator(
        key = FULL_NAME_KEY,
        rule = { value -> value.isBlank() },
        message = "Please add full name"
    )
}