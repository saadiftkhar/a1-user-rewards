package com.freespinslink.user.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.freespinslink.user.model.Rewards
import com.freespinslink.user.network.client.ApiService
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.RewardsApp

class RewardsPagingSource(private val apiService: ApiService) :
    PagingSource<Int, Rewards>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Rewards> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getRewards(
                pageNo = position.toString(),
                limit = params.loadSize.toString(),
                game = Constants.getGame(),
                androidId = RewardsApp.androidId
            )

            val pagedResponse = response.body()
            val data = pagedResponse?.data ?: emptyList()

            LoadResult.Page(
                data,
                prevKey = null,
                nextKey = if (data.isNotEmpty()) (position + 1) else null
            )
        } catch (e: Exception) {
            Log.d("Get_Reward", "error: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Rewards>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}



