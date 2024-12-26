package com.lavinou.startpoint.auth.social.google

import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class GoogleConfiguration {

    fun build(): Google {
        return Google()
    }
}