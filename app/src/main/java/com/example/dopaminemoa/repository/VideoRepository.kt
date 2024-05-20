package com.example.dopaminemoa.repository

import android.util.Log
import com.example.dopaminemoa.API_KEY
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.model.ChannelItemModel
import com.example.dopaminemoa.mapper.model.PopularVideoItemModel
import com.example.dopaminemoa.mapper.model.CategoryItemModel
import com.example.dopaminemoa.mapper.VideoItemMapper
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import retrofit2.HttpException

interface VideoRepository {
    suspend fun searchPopularVideo(): List<PopularVideoItemModel>
    suspend fun searchVideoByCategory(categoryId: String): List<PopularVideoItemModel>
    suspend fun takeVideoCategories(): List<CategoryItemModel>
    suspend fun searchChannelByCategory(category: String): List<ChannelItemModel>
    suspend fun searchVideoByText(text: String): Resource<List<VideoItemModel>>
}

class VideoRepositoryImpl(private val remoteDataSource: RemoteDataSource) : VideoRepository {
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
    override suspend fun takeVideoCategories(): List<CategoryItemModel> { //카테고리 목록
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

    //API exception
    private fun handleApiError(e: HttpException): String {
        return  when (e.code()) {
            400 -> "400 Error"
            401 -> "401 Error"
            403 -> "403 Error"
            404 -> "404 Error"
            else -> "알 수 없는 Error"
        }
//        Log.e("API error 확인", errorMessage)
    }
    class ApiException(message: String) : Exception(message)
}