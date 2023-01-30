package com.freespinslink.user.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.freespinslink.user.model.RewardViews
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.network.base.OneShotEvent
import com.freespinslink.user.repository.RewardsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RewardsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = RewardsRepo()

    val updateReward: MutableLiveData<OneShotEvent<Rewards>> = MutableLiveData()
    val updateRewardFailed: MutableLiveData<Boolean> = MutableLiveData()
    val rewards: MutableLiveData<PagingData<Rewards>?> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    private var result: Flow<PagingData<Rewards>>? = null

    fun fetchRewards() {

        viewModelScope.launch {
            if (rewards.value == null)
                result = repo.fetchRewards().cachedIn(viewModelScope)

            result?.let { data ->
                data.collectLatest {
                    rewards.postValue(it)
                }
            } ?: kotlin.run {
                error.postValue(true)
            }
        }
    }

    fun refresh() {
        rewards.value = null

        fetchRewards()
    }

    fun updateView(rewardViews: RewardViews) {
        viewModelScope.launch {
            val result = repo.updateViews(rewardViews)
            result.successResponse?.let {
                it.data?.let { reward ->
                    updateReward.postValue(OneShotEvent(reward))
                }

            } ?: result.errorResponse?.let {
                updateRewardFailed.postValue(true)
            }
        }
    }
}