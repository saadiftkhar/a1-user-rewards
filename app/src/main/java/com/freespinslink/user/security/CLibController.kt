package com.freespinslink.user.security

object CLibController {
    init {
        System.loadLibrary("native-rewards-lib")
    }

    external fun getGoogleAdIds(): Array<String>

    external fun getRewardsBaseUrl(): String

    external fun getFcmBaseUrl(): String

}