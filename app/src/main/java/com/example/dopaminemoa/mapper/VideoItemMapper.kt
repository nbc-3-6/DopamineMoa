package com.example.dopaminemoa.mapper

import com.example.dopaminemoa.data.eachResponse.SearchItems
import com.example.dopaminemoa.data.eachResponse.VideoItems

/**
 * api를 통해 응답 받은 데이터 중 view에서 사용할 데이터만 매핑하여 데이터 목록을 반환하는 함수입니다.
 */

object VideoItemMapper {
    fun fromSearchItems(searchItems: List<SearchItems>): List<VideoItemModel> {
        return searchItems.map {
            VideoItemModel(
                videoId = it.id.videoId,
                videoTitle = it.snippet.title,
                videoThumbnail = it.snippet.thumbnails.high.url,
                videoViews = "", // 조회수 데이터는 향후 처리
                videoDescription = it.snippet.description,
                channelTitle = it.snippet.channelTitle,
                channelThumbnails = "", // 채널 썸네일 데이터는 향후 처리
                categoryId = "",
            )
        }
    }

    fun fromPopularItems(searchItems: List<VideoItems>): List<PopularVideoItemModel> {
        return searchItems.map {
            PopularVideoItemModel(
                videoId = it.videoId,
                videoTitle = it.snippet.videoTitle,
                videoThumbnail = it.snippet.thumbnails.high.url,
                videoViews = "",
                videoDescription = it.snippet.description,
                channelTitle = it.snippet.channelTitle,
                channelThumbnails = "",
                categoryId = it.snippet.categoryId //카테고리 int값
//                channelId = it.snippet.channelId, //채널명
            )
        }
    }

//    fun updateVideoViews(videoItems: List<VideoItemModel>, videoListResponse: VideoListResponse) : List<VideoItemModel> {
//        return videoItems.map { videoItem ->
//            val video = videoListResponse.items.find { it.id == videoItem.videoId }
//            videoItem.copy(videoViews = video?.statistics?.viewCount ?: "")
//        }
//    }
//
//    fun updateChannelThumbnails(videoItems: List<VideoItemModel>, channelListResponse: ChannelListResponse) : List<VideoItemModel> {
//        return videoItems.map { videoItem ->
//            val channel = channelListResponse.items.find { it.id == videoItem.videoId }
//            videoItem.copy(channelThumbnails = channel?.snippet?.thumbnails.toString())
//        }
//    }
}