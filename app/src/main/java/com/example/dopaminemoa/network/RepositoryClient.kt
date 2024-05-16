package com.example.dopaminemoa.network

import com.example.dopaminemoa.data.remote.RemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Repository 세팅
object RepositoryClient {
    private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val youtubeService: RemoteDataSource by lazy { retrofit.create(RemoteDataSource::class.java) }
}