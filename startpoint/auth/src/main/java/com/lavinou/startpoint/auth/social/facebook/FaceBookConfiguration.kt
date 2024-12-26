package com.lavinou.startpoint.auth.social.facebook

import com.lavinou.startpoint.dsl.StartPointDsl

@StartPointDsl
class FaceBookConfiguration {

    fun build(): FaceBook {
        return FaceBook()
    }
}