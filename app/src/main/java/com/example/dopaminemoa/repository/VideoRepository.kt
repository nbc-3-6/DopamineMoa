package com.example.dopaminemoa.repository

import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.model.ChannelItemModel
import com.example.dopaminemoa.mapper.model.PopularVideoItemModel
import com.example.dopaminemoa.mapper.model.CategoryItemModel
import android.content.Context
import android.content.SharedPreferences
import com.example.dopaminemoa.Const
import com.example.dopaminemoa.mapper.VideoItemMapper
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException

interface VideoRepository {
    suspend fun searchPopularVideo(): List<PopularVideoItemModel>
    suspend fun searchVideoByCategory(categoryId: String): List<PopularVideoItemModel>
    suspend fun takeVideoCategories(): List<CategoryItemModel>
    suspend fun searchChannelByCategory(channelId: String): List<ChannelItemModel>
    suspend fun searchVideoByText(text: String): List<VideoItemModel>
    suspend fun searchMoreVideoByText(text: String, token: String): List<VideoItemModel>
    suspend fun saveVideoItem(videoItemModel: VideoItemModel)
    suspend fun removeVideoItem(videoItemModel: VideoItemModel)
    suspend fun getStorageItems(): List<VideoItemModel>
    fun isVideoLikedInPrefs(videoId: String?): Boolean
    fun clearPrefs()
}

class VideoRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val context: Context
) : VideoRepository {

    private val pref: SharedPreferences = context.getSharedPreferences(Const.PREFERENCE_NAME, 0)

    /**
     * 인기 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchPopularVideo(): List<PopularVideoItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getVideosList()
            VideoItemMapper.fromPopularItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

    /**
     * 선택한 카테고리에 대한 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchVideoByCategory(categoryId: String): List<PopularVideoItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getVideosByCategoryList(videoCategoryId = categoryId)
            VideoItemMapper.fromPopularItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

    /**
     * spinner에 카테고리를 넣기 위한 함수입니다.
     */
    override suspend fun takeVideoCategories(): List<CategoryItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getVideoCategoriesList()
            VideoItemMapper.fromCategoryItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

    /**
     * 선택한 카테고리에 대한 채널 검색 결과를 요청하는 함숩니다.
     */
    override suspend fun searchChannelByCategory(channelId: String): List<ChannelItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getChannelsList(channelId = channelId)
            VideoItemMapper.fromChannelItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

    /**
     * 입력된 검색어에 대한 검색 결과를 요청하는 함수입니다.
     * try-catch로 통신 결과를 처리합니다.
     * 통신 에러 발생 시 해당하는 에러 exception을 Resource.Error에 전달합니다.
     */
    override suspend fun searchVideoByText(text: String): List<VideoItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getSearchList(query = text)
            VideoItemMapper.fromSearchItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

    /**
     * 입력된 검색어에 대한 검색 결과를 추가적으로 요청하는 함수입니다.
     * 토큰을 이용하여 다음 페이지의 값들을 요청합니다.
     * try-catch로 통신 결과를 처리는 searchVideoByText 함수와 동일합니다.
     */
    override suspend fun searchMoreVideoByText(text: String, token: String): List<VideoItemModel> {
        return try {
            val videoListResponse = remoteDataSource.getSearchMoreList(query = text, pageToken = token)
            VideoItemMapper.fromSearchItems(videoListResponse.items)
        } catch (e: HttpException) {
            throw ApiException(handleApiError(e))
        }
    }

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

    //API exception
    private fun handleApiError(e: HttpException): String {
        return when (e.code()) {
            400 -> "400 Error"
            401 -> "401 Error"
            403 -> "403 Error"
            404 -> "404 Error"
            else -> "알 수 없는 Error"
        }
    }

    class ApiException(message: String) : Exception(message)
}