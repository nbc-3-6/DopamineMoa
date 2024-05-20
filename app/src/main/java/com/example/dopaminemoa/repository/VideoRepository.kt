package com.example.dopaminemoa.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.dopaminemoa.Const
import com.example.dopaminemoa.Const.Companion.API_KEY
import com.example.dopaminemoa.mapper.VideoItemMapper
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface VideoRepository {
    suspend fun searchPopularVideo(): List<VideoItemModel>
    suspend fun searchVideoByCategory(category: String): List<VideoItemModel>
    suspend fun searchChannelByCategory(category: String): List<VideoItemModel>
    suspend fun searchVideoByText(text: String): Resource<List<VideoItemModel>>
    suspend fun saveVideoItem(videoItemModel: VideoItemModel)
    suspend fun removeVideoItem(videoItemModel: VideoItemModel)

    fun isVideoLikedInPrefs(videoId: String?): Boolean

    suspend fun getStorageItems(): List<VideoItemModel>
    fun clearPrefs()
}

class VideoRepositoryImpl(context: Context): VideoRepository {
    /**
     * 인기 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchPopularVideo(): List<VideoItemModel> {
        TODO("Not yet implemented")
    }

    /**
     * 선택한 카테고리에 대한 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchVideoByCategory(category: String): List<VideoItemModel> {
        TODO("Not yet implemented")
    }

    /**
     * 선택한 카테고리에 대한 채널 검색 결과를 요청하는 함숩니다.
     */
    override suspend fun searchChannelByCategory(category: String): List<VideoItemModel> {
        TODO("Not yet implemented")
    }

    /**
     * 입력된 검색어에 대한 검색 결과를 요청하는 함수입니다.
     * try-catch로 통신 결과를 처리합니다.
     * 통신 에러 발생 시 해당하는 에러 exception을 Resource.Error에 전달합니다.
     */
    override suspend fun searchVideoByText(text: String): Resource<List<VideoItemModel>> {
        return try {
            val response = RepositoryClient.youtubeService.getSearchList("snippet", text, "video", API_KEY)
            if (response.items.isNotEmpty()) {
                Resource.Success(VideoItemMapper.fromSearchItems(response.items))
            } else {
                Resource.Error(NetworkException("No data found"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    private val pref: SharedPreferences = context.getSharedPreferences(Const.PREFERENCE_NAME, 0)

    // Gson()을 사용하여 Json 문자열을 VideoItemModel 객체로 변환
    private fun getPrefsItems(): List<VideoItemModel> {
        val jsonString = pref.getString(Const.LIKED_ITEMS, "")
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(jsonString, object : TypeToken<List<VideoItemModel>>() {}.type)
        }
    }

    // VideoItemModel 객체 아이템을 Json 문자열로 변환한 후 저장
    private fun savePrefsItems(items: List<VideoItemModel>) {
        val jsonString = Gson().toJson(items)
        pref.edit().putString(Const.LIKED_ITEMS, jsonString).apply()
    }

    override suspend fun saveVideoItem(videoItemModel: VideoItemModel) {
        val likedItems = getPrefsItems().toMutableList()
        val findItem = likedItems.find { it.videoId == videoItemModel.videoId }

        if (findItem == null) {
            likedItems.add(videoItemModel)
            savePrefsItems(likedItems)
        }
    }

    override suspend fun removeVideoItem(videoItemModel: VideoItemModel) {
        val likedItems = getPrefsItems().toMutableList()

        likedItems.removeAll { it.videoId == videoItemModel.videoId }
        savePrefsItems(likedItems)
    }

    // SharedPreferences에 저장된 모든 데이터 삭제
    override fun clearPrefs() {
        pref.edit().clear().apply()
    }

    // sharedPreference에 저장된 아이템들을 리스트로 가져옴
    override suspend fun getStorageItems(): List<VideoItemModel> {
        return getPrefsItems()
    }

    // 특정 videoId가 저장되어 있는지 확인
    override fun isVideoLikedInPrefs(videoId: String?): Boolean {
        val likedItems = getPrefsItems()
        return likedItems.any { it.videoId == videoId }
    }
}