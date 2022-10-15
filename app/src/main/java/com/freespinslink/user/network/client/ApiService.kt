package com.freespinslink.user.network.client

import com.freespinslink.user.model.RewardViews
import com.freespinslink.user.network.ApiEndPoints
import com.freespinslink.user.network.ApiParams
import com.freespinslink.user.network.responses.RewardViewsResponse
import com.freespinslink.user.network.responses.RewardsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST(ApiEndPoints.UPDATE_VIEWS)
    suspend fun updateViews(@Body rewardViews: RewardViews): Response<RewardViewsResponse>

    @GET(ApiEndPoints.GET_USER_REWARDS)
    suspend fun getRewards(
        @Query(ApiParams.PAGE_NO) pageNo: String,
        @Query(ApiParams.ITEMS_PER_PAGE) limit: String,
        @Query(ApiParams.GAME_NAME) game: String,
        @Query(ApiParams.ANDROID_ID) androidId: String
    ): Response<RewardsResponse>

}