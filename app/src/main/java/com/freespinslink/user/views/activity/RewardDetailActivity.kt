package com.freespinslink.user.views.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.freespinslink.user.R
import com.freespinslink.user.ads.unity.AdsConfig
import com.freespinslink.user.ads.unity.MediationManager
import com.freespinslink.user.bottomsheets.CopyRewardBottomSheet
import com.freespinslink.user.bottomsheets.OpenRewardBottomSheet
import com.freespinslink.user.databinding.ActivityRewardDetailBinding
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.Arguments
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.SharedStorage
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener

class RewardDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRewardDetailBinding

    private var rewardDetails: Rewards? = null

    private val mediationManager: MediationManager by lazy { MediationManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reward_detail)

        rewardDetails = intent?.getSerializableExtra(Arguments.REWARD_DETAILS) as Rewards
        showBannerAds(this, binding.bannerView)
        setupViews()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_back_press -> onBackPressedDispatcher.onBackPressed()
            R.id.tv_claim -> handleRewardsSheet()
        }
    }

    private fun setupViews() {

        binding.tvRewardName.text = rewardDetails?.title
        binding.tvDate.text = rewardDetails?.date
        binding.tvTime.text = rewardDetails?.time

        binding.ivBackPress.setOnClickListener(this)
        binding.tvClaim.setOnClickListener(this)

    }

    private fun handleRewardsSheet() {
        if (Constants.isAppInstalled(this)) {
            if (rewardDetails?.isRedeemCode() == true)
                showCopyCodeSheet()
            else
                showConsentSheet()
        } else {
            binding.tvError.let {
                it.isVisible = true
                it.text = this.getString(
                    R.string.app_not_installed,
                    Constants.getGameName,
                    Constants.getGameName
                )
            }
        }
    }

    private fun showCopyCodeSheet() {
        if (rewardDetails != null) {
            val dialog = CopyRewardBottomSheet()
            val bundle = Bundle()
            bundle.putSerializable("reward_details", rewardDetails)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "CopyReward")
        }
    }

    private fun showConsentSheet() {
        if (SharedStorage.isDialogShowAgain()) {
            rewardDetails?.reward_url?.let { url ->
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("https://") || url.startsWith("http://")) {
                    val uri: Uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show()
            }
        }
        } else {
            if (!isFinishing) {
                if (rewardDetails != null) {
                    val dialog = OpenRewardBottomSheet()
                    val bundle = Bundle()
                    bundle.putSerializable("reward_details", rewardDetails)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "CopyReward")
                }
            }
        }
    }

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
                handleRewardsSheet()
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
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial(AdsConfig.intPlacement)
        } else
            handleRewardsSheet()
    }

    fun showBannerAds(context: Context, bannerView: FrameLayout? = null) {
        val banner: IronSourceBannerLayout =
            IronSource.createBanner(context as Activity, ISBannerSize.RECTANGLE)
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
}