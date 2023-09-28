package com.freespinslink.user.ads.unity

import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.freespinslink.user.enums.EnumScreens
import com.freespinslink.user.listeners.InterstitialAdListener
import com.freespinslink.user.utils.AdsDebugConstants
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.InitializationListener
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener


class MediationManager(
    val listener: InterstitialAdListener,
    val context: FragmentActivity,
    val bannerView: FrameLayout
) : AdsConfig() {

    fun initAds() {
        IronSource.shouldTrackNetworkState(context, true)
        if (isTestMode) {
            IntegrationHelper.validateIntegration(context) // The integrationHelper is used to validate the integration. Remove the integrationHelper before going live!
            IronSource.setMetaData("is_test_suite", "enable")
        }

        SupersonicConfig.getConfigObj().clientSideCallbacks = true
        IronSource.setUserId(IronSource.getAdvertiserId(context))
        IronSource.init(context, AdsConfig().demoAppKey, object : InitializationListener {
            override fun onInitializationComplete() {
                IronSource.launchTestSuite(context)
                Log.d(javaClass.simpleName, "onInitializationComplete: true")
            }
        })

        Log.d(javaClass.simpleName, "initMediation: ")
        showBannerAds()
        loadInterstitialAd()

    }

    private fun showBannerAds() {
        val banner: IronSourceBannerLayout = IronSource.createBanner(context, ISBannerSize.BANNER)
        banner.let {
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            bannerView.addView(it, 0, layoutParams)
        }

        banner.levelPlayBannerListener = object : LevelPlayBannerListener {
            override fun onAdLoaded(adInfo: AdInfo) {
                Log.d("Banner_Ad_Result", "onAdLoaded: $adInfo")
                bannerView.isVisible = true
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
        IronSource.loadBanner(banner, bannerPlacement)
    }

    /*****************
    Interstitial Ad
     */
    private fun loadInterstitialAd() {
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
                listener.onIntAdCloseOrFail()
            }

            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
                IronSource.loadInterstitial()
                Log.d(javaClass.simpleName, "onIntAdShowFailed: ${error.errorMessage} \n $adInfo")
            }

            override fun onAdClicked(adInfo: AdInfo) {}

            override fun onAdShowSucceeded(adInfo: AdInfo) {}
        })

    }

    fun showIntAd(screen: String) {
        if (IronSource.isInterstitialReady()) {
            if (screen == EnumScreens.REWARDS.value)
                IronSource.showInterstitial(intPlacement1)
            else
                IronSource.showInterstitial(intPlacement2)
        } else
            listener.onIntAdCloseOrFail()
    }

}