package com.freespinslink.user.model

import java.io.Serializable

data class Rewards(
    var id: String = "",
    var reward_id: String = "",
    var title: String = "",
    var game_name: String = "",
    var reward_url: String = "",
    var views: String = "",
    var is_redeem_code: Int = 0,
    var opened: String? = "",
    var is_opened: String? = "",
    var android_id: String? = "",
    var date: String = "",
    var time: String = ""
) : Serializable {
    fun isRedeemCode(): Boolean {
        return is_redeem_code == 1
    }
}