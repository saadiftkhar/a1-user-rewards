package com.freespinslink.user.ads.applovinUnityMediation

import android.content.Context
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView

class BannerAdManager(val context: Context, private val bannerView: FrameLayout) :
    MaxAdViewAdListener {
    private var maxAdView: MaxAdView = MaxAdView(AdsConfig.bannerAdId, context)

    fun createBannerAd() {
        maxAdView.apply {
            bannerView.addView(this)
            loadAd()
            startAutoRefresh()
        }
    }

    fun destroy() {
        maxAdView.destroy()
    }

    override fun onAdLoaded(p0: MaxAd) {

    }

    override fun onAdDisplayed(p0: MaxAd) {
    }

    override fun onAdHidden(p0: MaxAd) {
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
    }

    override fun onAdExpanded(p0: MaxAd) {
    }

    override fun onAdCollapsed(p0: MaxAd) {
    }

}