/*
 *
 * Created by Saad Iftikhar on 10/11/21, 5:19 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freespinslink.user.R
import com.freespinslink.user.databinding.ItemRewardsBinding
import com.freespinslink.user.listeners.OnRewardOpen
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.viewholder.RewardsVH

class RewardsPagingAdapter(private val listener: OnRewardOpen) :
    PagingDataAdapter<Rewards, RecyclerView.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRewardsBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_rewards, parent, false)
        return RewardsVH(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RewardsVH) {
            holder.bind(getItem(position) ?: Rewards(), listener)
        }
    }

    fun update(newReward: Rewards, position: Int) {
        try {
            val oldReward = getItem(position)
            oldReward?.views = newReward.views
            oldReward?.opened = newReward.opened
            notifyItemChanged(position)
        } catch (e: Exception) {
        }
    }


}

class DiffUtilCallBack : DiffUtil.ItemCallback<Rewards>() {
    override fun areItemsTheSame(old: Rewards, new: Rewards): Boolean =
        old.id == new.id

    override fun areContentsTheSame(old: Rewards, new: Rewards): Boolean =
        old == new
}