@file:Suppress("DEPRECATION")

package com.freespinslink.user.ads.unity

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.freespinslink.user.listeners.UnityIntAdListener
import com.unity3d.ads.UnityAds
import com.unity3d.mediation.*
import com.unity3d.mediation.errors.LoadError
import com.unity3d.mediation.errors.SdkInitializationError
import com.unity3d.mediation.errors.ShowError
import com.unity3d.services.banners.IUnityBannerListener
import com.unity3d.services.banners.UnityBanners


class UnityMediationManager(
    val listener: UnityIntAdListener,
    val context: FragmentActivity,
    val bannerView: LinearLayout
) : UnityAdsConfig() {
    private var interstitialAd: InterstitialAd? = null
    private var isIntAdLoaded = false

    fun initMediation() {
        val configuration = InitializationConfiguration.builder()
            .setGameId(gameId)
            .setInitializationListener(object : IInitializationListener {
                override fun onInitializationComplete() {
                    // Unity Mediation is initialized. Try loading an ad.
                    Log.d("UnityAdsExample", "onInitializationComplete: success")
                    loadIntAd()
                }

                override fun onInitializationFailed(
                    errorCode: SdkInitializationError,
                    msg: String
                ) {
                    // Unity Mediation failed to initialize. Printing failure reason...
                    Log.d("Unity_Ads_mediation", "onInitializationFailed: $msg")

                    println("Unity Mediation Failed to Initialize : $msg")
                }
            }).build()
        UnityMediation.initialize(configuration)
    }


    private fun showBannerAds() {
        val bannerListener = object : IUnityBannerListener {
            override fun onUnityBannerLoaded(p0: String?, p1: View?) {
                bannerView.removeView(p1)
                bannerView.addView(p1)
                Log.d("Unity_Banner_Ad", "onUnityBannerLoaded: $p0")
            }

            override fun onUnityBannerUnloaded(p0: String?) {
                Log.d("Unity_Banner_Ad", "onUnityBannerUnloaded: $p0")
            }

            override fun onUnityBannerShow(p0: String?) {
                Log.d("Unity_Banner_Ad", "onUnityBannerShow: $p0")
            }

            override fun onUnityBannerClick(p0: String?) {

            }

            override fun onUnityBannerHide(p0: String?) {
                Log.d("Unity_Banner_Ad", "onUnityBannerHide: $p0")
            }

            override fun onUnityBannerError(p0: String?) {
                Log.d("Unity_Banner_Ad", "onUnityBannerError: $p0 ${UnityAds.isInitialized()}")
            }
        }

        UnityBanners.setBannerListener(bannerListener)
        UnityBanners.loadBanner(context, bannerPlacement)

    }

    /*****************
    Interstitial Ad
     */
    private val intLoadListener: IInterstitialAdLoadListener =
        object : IInterstitialAdLoadListener {
            override fun onInterstitialLoaded(ad: InterstitialAd) {
                // Execute logic when the ad successfully loads.
                isIntAdLoaded = true
                Log.d("UnityAdsExample", "onInterstitialLoaded: ")
            }

            override fun onInterstitialFailedLoad(
                ad: InterstitialAd,
                error: LoadError,
                msg: String
            ) {
                // Execute logic when the ad fails to load.
                isIntAdLoaded = false
                Log.d("UnityAdsExample", "onInterstitialFailedLoad: ")
                println("*******onInterstitialFailedLoad，msg：$msg")
            }
        }

    private val intShowListener: IInterstitialAdShowListener =
        object : IInterstitialAdShowListener {
            override fun onInterstitialShowed(interstitialAd: InterstitialAd) {
                // The ad has started to show.
                isIntAdLoaded = false
                Log.d("UnityAdsExample", "onInterstitialShowed: ")
                loadIntAd()
            }

            override fun onInterstitialClicked(interstitialAd: InterstitialAd) {
                isIntAdLoaded = false
                // The user has selected the ad.
                Log.d("UnityAdsExample", "onInterstitialClicked: ")
                loadIntAd()
            }

            override fun onInterstitialClosed(interstitialAd: InterstitialAd) {
                isIntAdLoaded = false
                loadIntAd()
                // The ad has finished showing.
                listener.onIntAdCloseOrFail()
                Log.d("UnityAdsExample", "onInterstitialClosed: ")
            }

            override fun onInterstitialFailedShow(
                interstitialAd: InterstitialAd,
                error: ShowError,
                msg: String
            ) {
                loadIntAd()
                listener.onIntAdCloseOrFail()
                isIntAdLoaded = false
                Log.e(
                    "UnityAdsExample",
                    "Int Ads failed with error: [$error] $msg"
                )
                // An error occurred during the ad playback.
            }
        }

    private fun loadIntAd() {
        interstitialAd = InterstitialAd(context, interstitialPlacement)
        interstitialAd?.load(intLoadListener)
    }

    fun showIntAd() {
        if (isIntAdLoaded)
            interstitialAd?.show(intShowListener)
        else
            listener.onIntAdCloseOrFail()
    }


}