package com.freespinslink.user.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import com.freespinslink.user.listeners.Bus
import com.freespinslink.user.utils.Constants.BUILD_FLAVOUR
import com.google.firebase.messaging.FirebaseMessaging

class RewardsApp : Application() {

    init {
    }

    companion object {

        var androidId = ""

    }

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("native-rewards-lib");

        setupInit()
        Constants.setupPPUrl()
        Constants.setupGamePackage()
    }

    private fun setupInit() {

        CommonConfig.initSharedConfig(configBus)
        SharedStorage.incSessionCount()

        FirebaseMessaging.getInstance()
            .subscribeToTopic(BUILD_FLAVOUR.lowercase()) // format -> dice_dreams
        androidId = getAndroidId()

    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return try {
            Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            "" // show dialog to restart the app
        }
    }


    private val configBus = object : Bus {
        override fun getAppContext(): Context {
            return this@RewardsApp.applicationContext
        }
    }

}