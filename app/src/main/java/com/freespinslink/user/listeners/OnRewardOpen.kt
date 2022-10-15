package com.freespinslink.user.listeners

import com.freespinslink.user.model.Rewards


interface OnRewardOpen {
    fun onOpen(rewards: Rewards, position: Int, type: String)
}