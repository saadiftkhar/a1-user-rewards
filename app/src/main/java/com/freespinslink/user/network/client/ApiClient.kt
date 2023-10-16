package com.freespinslink.user.network.client

import com.freespinslink.user.security.CLibController
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var retrofit: Retrofit? = null

    @Synchronized
    fun getInstance(): Retrofit {
        when (retrofit) {
            null -> retrofit = Retrofit.Builder()
                .baseUrl(CLibController.getRewardsBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build()
        }
        return retrofit as Retrofit
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(600, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(Interceptor { chain ->

                val builder = chain.request().newBuilder()
                builder.addHeader("Accept", "application/json")
                builder.addHeader("Content-Type", "application/json")
                chain.proceed(builder.build())

            }).addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

}