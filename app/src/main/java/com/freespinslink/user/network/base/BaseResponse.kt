package com.freespinslink.user.network.base

import com.google.gson.annotations.SerializedName

abstract class BaseResponse(
    @SerializedName("status")
    var code: Int? = 0,
    @SerializedName("status_message")
    var message: String? = "",
)
