package com.freespinslink.user.utils

import com.freespinslink.user.enums.EnumRating
import com.freespinslink.user.utils.Constants.BUILD_FLAVOUR

class RewardsConfig {
    companion object {
        const val PREF_NAME = "s1_${BUILD_FLAVOUR}_pref"
        val DEFAULT_RATING_STATE = EnumRating.NOT_RATED_YET.value
    }
}