package com.freespinslink.user.ads.applovinUnityMediation

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAppOpenAd
import com.applovin.sdk.AppLovinSdk

class OpenAppAdManager(
    val context: Context,
    val onLoaded: (Boolean) -> Unit,
    val onDisplayed: (Boolean) -> Unit,
    val onFailed: (Boolean) -> Unit,
    val onClose: (Boolean) -> Unit
) : LifecycleObserver, MaxAdListener {
    private var appOpenAd: MaxAppOpenAd

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        appOpenAd = MaxAppOpenAd("58faaadb59f3da34", context)
        appOpenAd.setListener(this)
        appOpenAd.loadAd()
    }

    private fun showAdIfReady() {
        if (AppLovinSdk.getInstance(context).isInitialized.not())
            return

        if (appOpenAd.isReady) {
            appOpenAd.showAd()
        } else {
            appOpenAd.loadAd()
        }
    }

    override fun onAdLoaded(ad: MaxAd) {
        showAdIfReady()
        onLoaded(true)
        Log.d("Aplovin_Open_App", "onAdLoaded: ${ad.adUnitId}")
    }

    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
        onFailed(true)
        Log.d("Aplovin_Open_App", "onAdLoadFailed: ${error.code} - ${error.message}")
    }

    override fun onAdDisplayed(ad: MaxAd) {
        Log.d("Aplovin_Open_App", "onAdDisplayed: ${ad.dspId}")

        onDisplayed(true)
    }

    override fun onAdClicked(ad: MaxAd) {}

    override fun onAdHidden(ad: MaxAd) {
        Log.d("Aplovin_Open_App", "onAdHidden: ${ad.dspId}")
        onClose(true)
//        appOpenAd.loadAd()
    }

    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
        Log.d("Aplovin_Open_App", "onAdDisplayFailed: ${ad.dspId}")
        onFailed(true)
        appOpenAd.loadAd()
    }
}
