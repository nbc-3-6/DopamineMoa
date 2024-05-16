package com.example.dopaminemoa.repository

import com.example.dopaminemoa.API_KEY
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.PopularVideoItemModel
import com.example.dopaminemoa.mapper.VideoItemMapper
import com.example.dopaminemoa.presentation.home.videocategory.toEntity
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.example.dopaminemoa.presentation.home.videocategory.VideoCategoryEntity

interface VideoRepository {
    suspend fun searchPopularVideo(): List<PopularVideoItemModel>
    suspend fun searchVideoByCategory(categoryId: String): List<PopularVideoItemModel>
    suspend fun fetchVideoCategories() : VideoCategoryEntity
    suspend fun searchChannelByCategory(category: String): List<VideoItemModel>
    suspend fun searchVideoByText(text: String): Resource<List<VideoItemModel>>
}

class VideoRepositoryImpl(private val remoteDataSource: RemoteDataSource) : VideoRepository {
    /**
     * 인기 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchPopularVideo(): List<PopularVideoItemModel> {
        val videoListResponse = remoteDataSource.getVideosList()
        return VideoItemMapper.fromPopularItems(videoListResponse.items)
    }

    /**
     * 선택한 카테고리에 대한 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchVideoByCategory(category:String): List<PopularVideoItemModel> {
        val videoListResponse = remoteDataSource.getVideosList(videoCategoryId = category)
        return VideoItemMapper.fromPopularItems(videoListResponse.items)
    }
    /**
     * spinner에 카테고리를 넣기 위한 함수입니다.
     */
    override suspend fun fetchVideoCategories(): VideoCategoryEntity { //카테고리 목록
        val videoListResponse = remoteDataSource.getVideoCategoriesList()
        return videoListResponse.toEntity()
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
}