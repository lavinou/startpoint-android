package com.lavinou.startpointapp.manager

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle

class StartPointAuthenticator(
    context: Context
) : AbstractAccountAuthenticator(context) {

    override fun editProperties(p0: AccountAuthenticatorResponse?, p1: String?): Bundle {
        throw UnsupportedOperationException()
    }

    override fun addAccount(
        p0: AccountAuthenticatorResponse?,
        p1: String?,
        p2: String?,
        p3: Array<out String>?,
        p4: Bundle?
    ): Bundle {
        val result = Bundle()
        result.putString(AccountManager.KEY_ACCOUNT_NAME, "Lavinou")
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, p2)
        return result
    }

    override fun confirmCredentials(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: Bundle?
    ): Bundle {
        return Bundle()
    }

    override fun getAuthToken(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: String?,
        p3: Bundle?
    ): Bundle {
        throw UnsupportedOperationException()
    }

    override fun getAuthTokenLabel(p0: String?): String {
        throw UnsupportedOperationException()
    }

    override fun updateCredentials(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: String?,
        p3: Bundle?
    ): Bundle {
        return Bundle()
    }

    override fun hasFeatures(
        p0: AccountAuthenticatorResponse?,
        p1: Account?,
        p2: Array<out String>?
    ): Bundle {
        TODO("Not yet implemented")
    }
}