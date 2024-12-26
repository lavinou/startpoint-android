package com.lavinou.startpointapp.auth.model

import com.lavinou.startpoint.auth.SPAuthUser
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val pk: Int,
    val username: String
)

sealed interface AppUser: SPAuthUser<Any> {

    data class AuthenticatedUser(
        override val id: String,
        override val value: User
    ) : AppUser {

    }

    data class AnonymousUser(
        override val id: String = "anonymous",
        override val value: Unit = Unit
    ) : AppUser

    val user: User?
        get() {
            return when(this) {
                is AuthenticatedUser -> this.value
                is AnonymousUser -> null
            }
        }

}