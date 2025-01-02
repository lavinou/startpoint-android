package com.lavinou.startpoint.auth

/**
 * Represents an authenticated user in the SPAuth system.
 *
 * @param T The type of value associated with the authenticated user.
 */
interface SPAuthUser<out T> {

    /**
     * The unique identifier of the authenticated user.
     */
    val id: String

    /**
     * The value or data associated with the authenticated user.
     */
    val value: T

}