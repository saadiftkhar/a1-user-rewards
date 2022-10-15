package com.freespinslink.user.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.freespinslink.user.model.RewardViews
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.network.base.BaseRepository
import com.freespinslink.user.network.base.ResponseModel
import com.freespinslink.user.network.client.ApiClient
import com.freespinslink.user.network.client.ApiService
import com.freespinslink.user.network.responses.RewardViewsResponse
import com.freespinslink.user.utils.Constants.PAGE_SIZE
import com.freespinslink.user.utils.Constants.PREFETCH_DISTANCE
import kotlinx.coroutines.flow.Flow

class RewardsRepo : BaseRepository() {
    private val apiService = ApiClient.getInstance().create(ApiService::class.java)

    fun fetchRewards(): Flow<PagingData<Rewards>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                /*
                same as page size because it's repeat data
                (it's load data according to initial value then on the basis of page size
                & must not be empty otherwise fetch data according to default formula)
                 */
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )
        ) {
            RewardsPagingSource(apiService)
        }.flow

    }

    suspend fun updateViews(rewardViews: RewardViews): ResponseModel<RewardViewsResponse> {
        return safeApiCall { apiService.updateViews(rewardViews) }
    }
}