package com.freespinslink.user.views.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.freespinslink.user.R
import com.freespinslink.user.ads.unity.MediationManager
import com.freespinslink.user.databinding.FragmentRewardDetailsBinding
import com.freespinslink.user.listeners.InterstitialAdListener
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.Arguments
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.SharedStorage

class RewardDetailsFragment : Fragment(), View.OnClickListener, InterstitialAdListener {


    private lateinit var binding: FragmentRewardDetailsBinding

    private lateinit var rewardDetails: Rewards

    private val navController by lazy { findNavController() }

    private val unityMediationManager: MediationManager by lazy { MediationManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rewardDetails = arguments?.get(Arguments.REWARD_DETAILS) as Rewards

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reward_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_back_press -> findNavController().popBackStack()
            R.id.tv_claim -> unityMediationManager.showIntAd()
        }
    }


    override fun onIntAdCloseOrFail() {
        handleRewardsSheet()
    }

    private fun setupViews() {

        binding.tvRewardName.text = rewardDetails.title
        binding.tvDate.text = rewardDetails.date
        binding.tvTime.text = rewardDetails.time

        binding.ivBackPress.setOnClickListener(this)
        binding.tvClaim.setOnClickListener(this)

    }

    private fun handleRewardsSheet() {
        if (Constants.isAppInstalled(requireContext())) {
            if (rewardDetails.isRedeemCode())
                showCopyCodeSheet()
            else
                showConsentSheet()
        } else {
            binding.tvError.let {
                it.isVisible = true
                it.text = requireActivity().getString(
                    R.string.app_not_installed,
                    Constants.getGameName,
                    Constants.getGameName
                )
            }
        }
    }

    private fun showCopyCodeSheet() {
        navController.navigate(
            RewardDetailsFragmentDirections.actionRewardDetailsFragmentToCopyRewardBottomSheet(
                rewardDetails
            )
        )
    }

    private fun showConsentSheet() {
        if (SharedStorage.isDialogShowAgain()) {
            val url = rewardDetails.reward_url
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("https://") || url.startsWith("http://")) {
                    val uri: Uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireActivity(), "Invalid Url", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), "Invalid Url", Toast.LENGTH_SHORT).show()
            }
        } else {
            navController.navigate(
                RewardDetailsFragmentDirections.actionRewardDetailsFragmentToOpenRewardBottomSheet(
                    rewardDetails
                )
            )

        }
    }


}