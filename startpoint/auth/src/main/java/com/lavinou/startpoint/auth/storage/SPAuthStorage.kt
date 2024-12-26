package com.lavinou.startpoint.auth.storage

import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import kotlinx.coroutines.flow.Flow

interface SPAuthStorage {

    val token: SPAuthToken?

    val tokenFlow: Flow<SPAuthToken?>

    fun save(token: SPAuthToken?)

    fun retrieve(): SPAuthToken
}