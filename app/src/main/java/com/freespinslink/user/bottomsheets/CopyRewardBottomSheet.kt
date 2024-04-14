/*
 *
 * Created by Saad Iftikhar on 8/25/21, 6:05 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freespinslink.user.R
import com.freespinslink.user.databinding.LayoutCopyRewardBinding
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.copyToClipboard
import com.freespinslink.user.utils.serializable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CopyRewardBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutCopyRewardBinding
    private lateinit var rewardDetails: Rewards

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rewardDetails = arguments?.serializable<Rewards>("reward_details") as Rewards

    }

    override fun getTheme(): Int = R.style.ChooseServiceBottomSheetTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireActivity(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutCopyRewardBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRedeemCode.setOnClickListener { onCopyToClipboard() }

    }

    private fun onCopyToClipboard() {
        requireActivity().copyToClipboard("Redeem_Code_Label", rewardDetails.reward_url)
    }

}