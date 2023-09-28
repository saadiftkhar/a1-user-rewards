package com.freespinslink.user.views.fragment

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.freespinslink.user.R
import com.freespinslink.user.ads.unity.AdsConfig
import com.freespinslink.user.ads.unity.MediationManager
import com.freespinslink.user.controller.RatingController
import com.freespinslink.user.databinding.FragmentRewardsBinding
import com.freespinslink.user.views.dialog.ProgressDialog
import com.freespinslink.user.enums.EnumCtaType
import com.freespinslink.user.enums.EnumScreens
import com.freespinslink.user.listeners.OnRewardOpen
import com.freespinslink.user.listeners.InterstitialAdListener
import com.freespinslink.user.model.RewardViews
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.*
import com.freespinslink.user.utils.Constants.SUPPORT_EMAIL
import com.freespinslink.user.utils.Constants.getPPUrl
import com.freespinslink.user.viewmodel.RewardsViewModel
import com.freespinslink.user.views.activity.StartupActivity
import com.freespinslink.user.views.adapter.RewardsPagingAdapter
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.sdk.InitializationListener
import kotlinx.coroutines.launch


class RewardsFragment : Fragment(), OnRewardOpen, View.OnClickListener, InterstitialAdListener {

    private lateinit var binding: FragmentRewardsBinding


    private var selectedPosition: Int = -1
    private lateinit var rewardCtaType: String

    private val rewardsViewModel: RewardsViewModel by activityViewModels()

    private val rewardsAdapter by lazy { RewardsPagingAdapter(this) }

    private lateinit var progressDialog: ProgressDialog

    private val ratingController: RatingController by lazy { RatingController(requireActivity()) }

    private lateinit var selectedReward: Rewards
    private val navController by lazy { findNavController() }

    private val unityMediationManager: MediationManager by lazy {
        MediationManager(this, requireActivity(), binding.bannerView)
    }


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

    override fun onIntAdCloseOrFail() {
        openDetails()
    }

    private fun setupInit() {
        progressDialog = ProgressDialog(requireContext())
        unityMediationManager.initAds()
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
                unityMediationManager.showIntAd(EnumScreens.REWARDS.value)
            }
        }

        rewardsViewModel.updateRewardFailed.observe(viewLifecycleOwner) {
            if (it) {
                unityMediationManager.showIntAd(EnumScreens.REWARDS.value)
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
        rewardViews.android_id = RewardsApp.androidId
        rewardsViewModel.updateView(rewardViews)
    }

    private fun openDetails() {
        rewardsViewModel.updateRewardFailed.postValue(false)
        Log.d("RewardsFragment", "openDetails: ${navController.currentDestination}")
        navController.navigate(
            RewardsFragmentDirections.actionRewardsFragmentToRewardDetailsFragment(
                selectedReward
            )
        )
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
                    unityMediationManager.showIntAd(EnumScreens.REWARDS.value)
                }

            }
            .setNegativeButton(requireContext().getString(R.string.ad_decision_negative_btn)) { dialog, which -> }
            .show()

    }
}