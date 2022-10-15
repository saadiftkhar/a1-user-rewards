package com.freespinslink.user.utils

import android.content.Context
import com.freespinslink.user.listeners.Bus


object CommonConfig {
    private lateinit var mBus: Bus

    fun initSharedConfig(bus: Bus) {
        synchronized(this) {
            mBus = bus
        }
    }

    fun getAppContext(): Context {
        return mBus.getAppContext()
    }
}