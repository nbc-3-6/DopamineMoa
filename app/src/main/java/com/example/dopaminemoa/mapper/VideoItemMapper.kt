package com.example.dopaminemoa.mapper

import com.example.dopaminemoa.data.eachResponse.ChannelListResponse
import com.example.dopaminemoa.data.eachResponse.SearchItems
import com.example.dopaminemoa.data.eachResponse.SearchListResponse
import com.example.dopaminemoa.data.eachResponse.VideoListResponse
import com.example.dopaminemoa.repository.VideoRepository

/**
 * api를 통해 응답 받은 데이터 중 view에서 사용할 데이터만 매핑하여 데이터 목록을 반환하는 함수입니다.
 */

object VideoItemMapper {
    fun fromSearchItems(searchItems: List<SearchItems>): List<VideoItemModel> {
        return searchItems.map {
            VideoItemModel(
                videoId = it.id.videoId,
                videoTitle = it.snippet.title,
                videoThumbnail = it.snippet.thumbnails.toString(),
                videoViews = "",
                videoDescription = it.snippet.description,
                channelTitle = it.snippet.channelTitle,
                channelThumbnails = ""
            )
        }
    }

    fun updateVideoViews(videoItems: List<VideoItemModel>, videoListResponse: VideoListResponse) : List<VideoItemModel> {
        return videoItems.map { videoItem ->
            val video = videoListResponse.items.find { it.id == videoItem.videoId }
            videoItem.copy(videoViews = video?.statistics?.viewCount ?: "")
        }
    }

    fun updateChannelThumbnails(videoItems: List<VideoItemModel>, channelListResponse: ChannelListResponse) : List<VideoItemModel> {
        return videoItems.map { videoItem ->
            val channel = channelListResponse.items.find { it.id == videoItem.videoId }
            videoItem.copy(channelThumbnails = channel?.snippet?.thumbnails.toString())
        }
    }
}