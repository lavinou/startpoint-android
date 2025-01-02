package com.lavinou.startpoint.auth.storage

import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class OptOutSPAuthStorage : SPAuthStorage {

    override val token: SPAuthToken?
        get() = null

    override val tokenFlow: Flow<SPAuthToken?>
        get() = flowOf(null)

    override fun save(token: SPAuthToken?) {
        throw UnsupportedOperationException("save not implemented. Currently using OptOutSPAuthStorage")
    }

    override fun retrieve(): SPAuthToken {
        throw UnsupportedOperationException("retrieve not implemented. Currently using OptOutSPAuthStorage")
    }
}