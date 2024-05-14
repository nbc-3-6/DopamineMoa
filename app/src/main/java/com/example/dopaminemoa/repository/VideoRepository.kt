package com.example.dopaminemoa.repository

import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.presentation.home.video.mapper.toEntity
import com.example.dopaminemoa.presentation.home.video.model.MostPopularItemEntity
import com.example.dopaminemoa.mapper.VideoItemModel

interface VideoRepository {
    suspend fun searchPopularVideo(): MostPopularItemEntity
    suspend fun searchVideoByCategory(category: String): List<VideoItemModel>
    suspend fun searchChannelByCategory(category: String): List<VideoItemModel>
    suspend fun searchVideoByText(text: String): List<VideoItemModel>
}

class VideoRepositoryImpl(private val remoteDataSource: RemoteDataSource): VideoRepository {
    /**
     * 인기 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchPopularVideo(): MostPopularItemEntity {
        val videoListResponse = remoteDataSource.getVideosList()

        return videoListResponse.toEntity()
        //return API킅라이언트.네트워크인터페이스.네트워크함수().데이터.asVideoItemModel()
    }

    /**
     * 선택한 카테고리에 대한 비디오 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchVideoByCategory(category: String): List<VideoItemModel> {
        TODO("Not yet implemented")
        //return API킅라이언트.네트워크인터페이스.네트워크함수(category).데이터.asVideoItemModel()
    }

    /**
     * 선택한 카테고리에 대한 채널 검색 결과를 요청하는 함숩니다.
     */
    override suspend fun searchChannelByCategory(category: String): List<VideoItemModel> {
        TODO("Not yet implemented")
        //return API킅라이언트.네트워크인터페이스.네트워크함수(category).데이터.asVideoItemModel()
    }

    /**
     * 입력된 검색어에 대한 검색 결과를 요청하는 함수입니다.
     */
    override suspend fun searchVideoByText(text: String): List<VideoItemModel> {
        TODO("Not yet implemented")
        //return API킅라이언트.네트워크인터페이스.네트워크함수(text).데이터.asVideoItemModel()
    }
}