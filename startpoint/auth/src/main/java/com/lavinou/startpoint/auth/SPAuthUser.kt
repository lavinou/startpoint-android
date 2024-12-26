package com.lavinou.startpoint.auth

interface SPAuthUser<out T> {

    val id: String

    val value: T

}