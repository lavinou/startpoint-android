package com.lavinou.startpoint.auth.storage

import android.content.Context
import com.lavinou.startpoint.auth.backend.model.SPAuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.E

class DefaultSPAuthStorage(
    context: Context
) : SPAuthStorage {

    companion object {
        private const val ALIAS = "start-point-auth-storage"
        private const val ACCESS_KEY = "start-point-auth-access"
        private const val REFRESH_KEY = "start-point-auth-refresh"
        private const val EXPIRES_AT_KEY = "start-point-auth-expires-at"
        private const val DEFAULT_EXPIRES_AT = 0L
    }

    private val _token: MutableStateFlow<SPAuthToken?> = MutableStateFlow(null)

    override val token: SPAuthToken?
        get() = _token.value

    override val tokenFlow: Flow<SPAuthToken?>
        get() = _token

    private val preferences =
        context.getSharedPreferences(ALIAS, Context.MODE_PRIVATE)

    override fun save(token: SPAuthToken?) {
        preferences.edit().apply {
            putString(ACCESS_KEY, token?.accessToken)
            putString(REFRESH_KEY, token?.refreshToken)
            putLong(EXPIRES_AT_KEY, token?.expiresAt ?: DEFAULT_EXPIRES_AT)
        }.apply()
        _token.update {
            token
        }
    }

    override fun retrieve(): SPAuthToken {
        val result = object : SPAuthToken {
            override val accessToken: String
                get() = preferences.getString(ACCESS_KEY, "").toString()

            override val refreshToken: String
                get() = preferences.getString(REFRESH_KEY, "").toString()
            override val expiresAt: Long
                get() = preferences.getLong(EXPIRES_AT_KEY, DEFAULT_EXPIRES_AT)
        }

        _token.update {
            result
        }
        return result
    }
}