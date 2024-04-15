/*
 *
 * Created by Saad Iftikhar on 8/25/21, 6:05 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.bottomsheets

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.freespinslink.user.R
import com.freespinslink.user.databinding.LayoutOpenRewardConsentBinding
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.utils.SharedStorage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class OpenRewardBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutOpenRewardConsentBinding
    private lateinit var rewardDetails: Rewards

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rewardDetails = arguments?.get("reward_details") as Rewards

    }

    override fun getTheme(): Int = R.style.ChooseServiceBottomSheetTheme


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireActivity(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutOpenRewardConsentBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set message
        val message = String.format(
            requireActivity().getString(R.string.consent_dialog_message),
            rewardDetails.title
        )
        binding.tvMessage.text = message

        // Click listener
        binding.tvNo.setOnClickListener { dismiss() }
        binding.tvYes.setOnClickListener { saveReward() }
    }

    private fun saveReward() {
        val url = rewardDetails.reward_url

        if (url.isNotEmpty()) {
            if (url.startsWith("https://") or url.startsWith("http://")) {
                SharedStorage.saveNotShowAgain(binding.cbNotSeeAgain.isChecked)

                val uri: Uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                requireActivity().startActivity(intent)
                dismiss()
            } else {
                Toast.makeText(requireActivity(), "Invalid Url", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireActivity(), "Invalid Url", Toast.LENGTH_SHORT).show()
        }
    }


}