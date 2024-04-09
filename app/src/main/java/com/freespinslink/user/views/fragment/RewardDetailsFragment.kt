package com.freespinslink.user.views.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.freespinslink.user.R
import com.freespinslink.user.databinding.FragmentRewardDetailsBinding
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.Arguments
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.SharedStorage
import com.freespinslink.user.utils.serializable
import com.saadiftkhar.toaster.Toaster

class RewardDetailsFragment : Fragment(), View.OnClickListener {


    private lateinit var binding: FragmentRewardDetailsBinding

    private var rewardDetails: Rewards? = null

    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rewardDetails = it.serializable<Rewards>(Arguments.REWARD_DETAILS)
        }
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
        if (Constants.isAppInstalled(requireContext())) {
            if (rewardDetails?.isRedeemCode() == true)
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
            rewardDetails?.reward_url?.let { url ->
                if (url.isNotEmpty()) {
                    if (url.startsWith("https://") || url.startsWith("http://")) {
                        val uri: Uri = Uri.parse(url)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } else {
                        Toaster.error(requireContext(), "Invalid Url")
                    }
                } else {
                    Toaster.error(requireContext(), "Invalid Url")
                }
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