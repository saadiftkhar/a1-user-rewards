package com.freespinslink.user.ads.unity

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.freespinslink.user.enums.EnumScreens
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener


class MediationManager {

    fun showBannerAds(context: Context, bannerView: FrameLayout? = null) {
        val banner: IronSourceBannerLayout =
            IronSource.createBanner(context as Activity, ISBannerSize.BANNER)
        banner.let {
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            bannerView?.addView(it, 0, layoutParams)
        }

        banner.levelPlayBannerListener = object : LevelPlayBannerListener {
            override fun onAdLoaded(adInfo: AdInfo) {
                Log.d("Banner_Ad_Result", "onAdLoaded: $adInfo")
                bannerView?.isVisible = true
            }

            override fun onAdLoadFailed(error: IronSourceError) {
                Log.d(
                    "Banner_Ad_Result",
                    "onAdLoadFailed: ${error.errorCode} - ${error.errorMessage}"
                )
            }

            override fun onAdClicked(adInfo: AdInfo) {}

            override fun onAdScreenPresented(adInfo: AdInfo) {}

            override fun onAdScreenDismissed(adInfo: AdInfo) {}

            override fun onAdLeftApplication(adInfo: AdInfo) {}
        }
        IronSource.loadBanner(banner, AdsConfig.bannerPlacement)
    }

    /*****************
    Interstitial Ad
     */
    fun loadInterstitialAd() {
        IronSource.loadInterstitial()
        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
            override fun onAdReady(adInfo: AdInfo) {
                Log.d("Interstitial_Ad_Result", "onAdReady: $adInfo")
            }

            override fun onAdLoadFailed(error: IronSourceError) {
                Log.d(
                    "Interstitial_Ad_Result",
                    "onAdLoadFailed: ${error.errorCode} - ${error.errorMessage}"
                )
            }

            override fun onAdOpened(adInfo: AdInfo) {}

            override fun onAdClosed(adInfo: AdInfo) {
                IronSource.loadInterstitial()
                Log.d(javaClass.simpleName, "onIntAdClosed: $adInfo")
//                adCLoseOrFail(true)
            }

            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                IronSource.loadInterstitial()
                Log.d(javaClass.simpleName, "onIntAdShowFailed: ${error.errorMessage} \n $adInfo")
            }

            override fun onAdClicked(adInfo: AdInfo) {}

            override fun onAdShowSucceeded(adInfo: AdInfo) {}
        })

    }

    fun showIntAd() {
//        if (IronSource.isInterstitialReady()) {
//            IronSource.showInterstitial(intPlacement)
//        } else
//            adCLoseOrFail(true)
    }

}