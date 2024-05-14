package com.example.dopaminemoa.repository

import com.example.dopaminemoa.API_KEY
import com.example.dopaminemoa.mapper.VideoItemMapper
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient

interface VideoRepository {
    suspend fun searchPopularVideo(): List<VideoItemModel>
    suspend fun searchVideoByCategory(category: String): List<VideoItemModel>
    suspend fun searchChannelByCategory(category: String): List<VideoItemModel>
    suspend fun searchVideoByText(text: String): List<VideoItemModel>
}

class VideoRepositoryImpl: VideoRepository {
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
     */
    override suspend fun searchVideoByText(text: String): List<VideoItemModel> {
        return VideoItemMapper.fromSearchItems(RepositoryClient.youtubeService.getSearchList("snippet", text, "video", API_KEY).items)
    }
}