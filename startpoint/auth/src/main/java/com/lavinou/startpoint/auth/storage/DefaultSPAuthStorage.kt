package com.lavinou.startpoint.auth.storage

import android.content.Context
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class DefaultSPAuthStorage(
    context: Context
) : SPAuthStorage {


    private val _token: MutableStateFlow<SPAuthToken?> = MutableStateFlow(null)

    override val token: SPAuthToken?
        get() = _token.value

    override val tokenFlow: Flow<SPAuthToken?>
        get() = _token

    private val preferences =
        context.getSharedPreferences("start-point-auth-storage", Context.MODE_PRIVATE)

    override fun save(token: SPAuthToken?) {
        preferences.edit().apply {
            putString("start-point-auth-access", token?.accessToken)
            putString("start-point-auth-refresh", token?.refreshToken)
        }.apply()
        _token.update {
            token
        }
    }

    override fun retrieve(): SPAuthToken {
        val result = object : SPAuthToken {
            override val accessToken: String
                get() = preferences.getString("start-point-auth-access", "").toString()

            override val refreshToken: String
                get() = preferences.getString("start-point-auth-refresh", "").toString()
            override val expiresAt: Long
                get() = System.currentTimeMillis() + 300000
        }

        _token.update {
            result
        }
        return result
    }
}