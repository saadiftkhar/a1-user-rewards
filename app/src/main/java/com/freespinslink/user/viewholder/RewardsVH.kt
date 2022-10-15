/*
 *
 * Created by Saad Iftikhar on 10/11/21, 5:20 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.freespinslink.user.R
import com.freespinslink.user.databinding.ItemRewardsBinding
import com.freespinslink.user.enums.EnumCtaType
import com.freespinslink.user.listeners.OnRewardOpen
import com.freespinslink.user.model.Rewards

class RewardsVH(private val binding: ItemRewardsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(rewards: Rewards, listener: OnRewardOpen) {

        binding.tvRewardTitle.text = rewards.title
        binding.tvRewardDateTime.text = rewards.date
        binding.tvRewardViews.text = rewards.views

        val ctaType: String

        if (rewards.opened == "1") {
            ctaType = EnumCtaType.NO_API_CALL.value
            binding.tvOpen.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_disable)
        } else {
            ctaType = EnumCtaType.API_CALL.value
            binding.tvOpen.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.bg_enable)
        }

        binding.tvOpen.setOnClickListener {
            listener.onOpen(rewards, adapterPosition, ctaType)
        }

    }

}