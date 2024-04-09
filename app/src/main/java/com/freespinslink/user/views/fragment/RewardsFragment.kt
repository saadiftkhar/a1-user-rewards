package com.freespinslink.user.views.fragment

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.freespinslink.user.R
import com.freespinslink.user.controller.RatingController
import com.freespinslink.user.databinding.FragmentRewardsBinding
import com.freespinslink.user.enums.EnumCtaType
import com.freespinslink.user.listeners.OnRewardOpen
import com.freespinslink.user.model.RewardViews
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.Arguments
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.Constants.SUPPORT_EMAIL
import com.freespinslink.user.utils.Constants.getPPUrl
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.utils.openInBrowser
import com.freespinslink.user.utils.showToast
import com.freespinslink.user.viewmodel.RewardsViewModel
import com.freespinslink.user.views.activity.RewardDetailActivity
import com.freespinslink.user.views.adapter.RewardsPagingAdapter
import com.freespinslink.user.views.dialog.ProgressDialog
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.pow


class RewardsFragment : Fragment(), OnRewardOpen, View.OnClickListener, MaxAdListener {

    private lateinit var binding: FragmentRewardsBinding

    private lateinit var interstitialAd: MaxInterstitialAd

    private var selectedPosition: Int = -1
    private lateinit var rewardCtaType: String

    private val rewardsViewModel: RewardsViewModel by activityViewModels()

    private val rewardsAdapter by lazy { RewardsPagingAdapter(this) }

    private lateinit var progressDialog: ProgressDialog

    private val ratingController: RatingController by lazy { RatingController(requireActivity()) }

    private var selectedReward: Rewards? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rewards, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showBannerAds(requireContext(), binding.bannerView)
        createInterstitialAd()

        setupInit()
        setupViews()
        setupObservers()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_rating -> ratingController.redirectToPlayStore()
            R.id.iv_share -> onClickShare()
            R.id.iv_privacy_policy -> getPPUrl.openInBrowser(requireActivity())
            R.id.iv_support -> onClickSupport()
        }
    }

    override fun onOpen(rewards: Rewards, position: Int, type: String) {
        if (Constants.isAppInstalled(requireContext())) {
            rewardCtaType = type
            selectedReward = rewards
            selectedPosition = position

            adDecision(rewards)
        } else {
            findNavController().navigate(RewardsFragmentDirections.actionRewardsFragmentToRouteToPlaystoreDialog())
        }
    }

    private fun setupInit() {
        progressDialog = ProgressDialog(requireContext())
        setupRecyclerView()
    }

    private fun setupViews() {

        if (SharedStorage.isRatingApplicable())
            RatingController(requireActivity()).showRateAppDialog()

        rewardsViewModel.fetchRewards()

        binding.swipeRefresh.setOnRefreshListener {
            rewardsViewModel.refresh()
        }

        binding.ivRating.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.ivPrivacyPolicy.setOnClickListener(this)
        binding.ivSupport.setOnClickListener(this)


    }

    private fun setupObservers() {

        rewardsViewModel.rewards.observe(viewLifecycleOwner) {
            if (it != null)
                lifecycleScope.launch {
                    rewardsAdapter.submitData(it)
                }
        }

        rewardsViewModel.updateReward.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { reward ->
                progressDialog.dismiss()
                selectedReward = reward
                rewardsAdapter.update(reward, selectedPosition)
                showIntAd()
            }
        }

        rewardsViewModel.updateRewardFailed.observe(viewLifecycleOwner) {
            if (it) {
                showIntAd()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvRewards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rewardsAdapter
            setHasFixedSize(true) // it's necessary otherwise load calls recursively
        }

        rewardsAdapter.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> binding.swipeRefresh.isRefreshing = true

                loadState.append is LoadState.Loading -> {
                    binding.progressBar.isVisible = true
                }

                else -> {
                    binding.progressBar.isVisible = false
                    binding.swipeRefresh.isRefreshing = false
                    progressDialog.dismiss()

                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        requireContext().showToast(it.error.toString())
                    }

                }
            }
        }
    }

    private fun onUpdateReward(id: String) {
        val rewardViews = RewardViews()
        rewardViews.reward_id = id
        rewardViews.android_id = SharedStorage.getAndroidId()
        rewardsViewModel.updateView(rewardViews)
    }


    private fun openDetails() {
        rewardsViewModel.updateRewardFailed.postValue(false)

        if (selectedReward != null) {
            findNavController().navigate(R.id.rewardDetailsFragment, Bundle().apply {
                putSerializable(Arguments.REWARD_DETAILS, selectedReward)
            })
        }

    }

    private fun onClickShare() {
        val text =
            "*${requireActivity().getString(R.string.share_link_title)}*" +
                    "\n\n${requireActivity().getString(R.string.share_link_message)}" +
                    "\n\n${Constants.PLAY_STORE_BASE_URL}${requireActivity().packageName}"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun onClickSupport() {
        val intent = Intent(ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$SUPPORT_EMAIL")

        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            requireActivity().getString(R.string.app_name)
        )
        startActivity(Intent.createChooser(intent, "Send mail..."))
    }

    private fun adDecision(rewards: Rewards) {

        AlertDialog.Builder(requireContext())
            .setTitle("")
            .setMessage(requireContext().getString(R.string.ad_decision_desc))
            .setCancelable(false)
            .setPositiveButton(requireContext().getString(R.string.ad_decision_positive_btn)) { dialog, which ->
                progressDialog.show()

                if (rewardCtaType == EnumCtaType.API_CALL.value) {
                    onUpdateReward(rewards.id)
                } else if (rewardCtaType == EnumCtaType.NO_API_CALL.value) {
                    progressDialog.dismiss()
                    showIntAd()
                }

            }
            .setNegativeButton(requireContext().getString(R.string.ad_decision_negative_btn)) { dialog, which -> }
            .show()

    }

    //    fun showBannerAds(context: Context, bannerView: FrameLayout? = null) {
//        val banner: IronSourceBannerLayout =
//            IronSource.createBanner(context as Activity, ISBannerSize.BANNER)
//        banner.let {
//            val layoutParams = FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//            )
//            bannerView?.addView(it, 0, layoutParams)
//        }
//
//        banner.levelPlayBannerListener = object : LevelPlayBannerListener {
//            override fun onAdLoaded(adInfo: AdInfo) {
//                Log.d("Banner_Ad_Result", "onAdLoaded: $adInfo")
//                bannerView?.isVisible = true
//            }
//
//            override fun onAdLoadFailed(error: IronSourceError) {
//                Log.d(
//                    "Banner_Ad_Result",
//                    "onAdLoadFailed: ${error.errorCode} - ${error.errorMessage}"
//                )
//            }
//
//            override fun onAdClicked(adInfo: AdInfo) {}
//
//            override fun onAdScreenPresented(adInfo: AdInfo) {}
//
//            override fun onAdScreenDismissed(adInfo: AdInfo) {}
//
//            override fun onAdLeftApplication(adInfo: AdInfo) {}
//        }
////        IronSource.loadBanner(banner, AdsConfig.bannerPlacement)
//    }
//    fun loadInterstitialAd() {
//        IronSource.loadInterstitial()
//        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
//            override fun onAdReady(adInfo: AdInfo) {
//                Log.d("Interstitial_Ad_Result", "onAdReady: $adInfo")
//            }
//
//            override fun onAdLoadFailed(error: IronSourceError) {
//                Log.d(
//                    "Interstitial_Ad_Result",
//                    "onAdLoadFailed: ${error.errorCode} - ${error.errorMessage}"
//                )
//            }
//
//            override fun onAdOpened(adInfo: AdInfo) {}
//
//            override fun onAdClosed(adInfo: AdInfo) {
//                openDetails()
//                IronSource.loadInterstitial()
//                Log.d(javaClass.simpleName, "onIntAdClosed: $adInfo")
//            }
//
//            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {
//                IronSource.loadInterstitial()
//                Log.d(javaClass.simpleName, "onIntAdShowFailed: ${error.errorMessage} \n $adInfo")
//            }
//
//            override fun onAdClicked(adInfo: AdInfo) {}
//
//            override fun onAdShowSucceeded(adInfo: AdInfo) {}
//        })
//
//    }
// MAX Ad Listener
    override fun onAdLoaded(maxAd: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        interstitialAd.loadAd()
        openDetails()
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd()
    }

    override fun onAdDisplayed(maxAd: MaxAd) {}

    override fun onAdClicked(maxAd: MaxAd) {
        Log.d("ad_ghjkh", "onAdClicked: ${maxAd.adUnitId}")
    }

    override fun onAdHidden(maxAd: MaxAd) {
        openDetails()
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd()
    }

  private  fun createInterstitialAd() {
        interstitialAd = MaxInterstitialAd("182d3d3e47a182f4", requireActivity())
        interstitialAd.setListener(this)

        // Load the first ad
        interstitialAd.loadAd()
    }

    private fun showIntAd() {
        if (interstitialAd.isReady) {
            interstitialAd.showAd()
        } else {
            openDetails()
        }
    }

}