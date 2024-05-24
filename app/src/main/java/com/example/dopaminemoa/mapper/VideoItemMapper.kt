package com.example.dopaminemoa.mapper

import com.example.dopaminemoa.data.eachResponse.ChannelItem
import com.example.dopaminemoa.data.eachResponse.SearchItems
import com.example.dopaminemoa.data.eachResponse.SearchListResponse
import com.example.dopaminemoa.data.eachResponse.VideoCategoryItems
import com.example.dopaminemoa.data.eachResponse.VideoItems
import com.example.dopaminemoa.mapper.model.ChannelItemModel
import com.example.dopaminemoa.mapper.model.PopularVideoItemModel
import com.example.dopaminemoa.mapper.model.CategoryItemModel
import com.example.dopaminemoa.mapper.model.VideoItemModel

/**
 * api를 통해 응답 받은 데이터 중 view에서 사용할 데이터만 매핑하여 데이터 목록을 반환하는 함수입니다.
 */

object VideoItemMapper {
    fun fromSearchItems(responses: SearchListResponse): Pair<List<VideoItemModel>, String> {
        val videoItems = mutableListOf<VideoItemModel>()
        var nextPageToken = ""

        nextPageToken = responses.nextPageToken
        videoItems.addAll(responses.items.map { item ->
            VideoItemModel(
                videoId = item.id.videoId,
                videoTitle = item.snippet.title,
                videoThumbnail = item.snippet.thumbnails.high.url,
                videoDescription = item.snippet.description,
                channelTitle = item.snippet.channelTitle,
                category = "",
                channelId = "",
                isLiked = false // 기본값 추가
            )
        })
        return Pair(videoItems, nextPageToken)
    }

    fun fromPopularItems(searchItems: List<VideoItems>): List<PopularVideoItemModel> {
        return searchItems.map {
            PopularVideoItemModel(
                videoId = it.videoId,
                videoTitle = it.snippet.videoTitle,
                videoThumbnail = it.snippet.thumbnails.high.url,
                videoDescription = it.snippet.description,
                channelTitle = it.snippet.channelTitle,
                category = it.snippet.category, //카테고리 int값
                channelId = it.snippet.channelId, //채널명
                isLiked = false, // 기본값 추가
            )
        }
    }

    fun PopularVideoItemModel.toVideoItemModel(): VideoItemModel {
        return VideoItemModel(
            videoId = this.videoId,
            videoTitle = this.videoTitle,
            videoThumbnail = this.videoThumbnail,
            videoDescription = this.videoDescription,
            channelTitle = this.channelTitle,
            category = this.category,
            channelId = this.channelId,
            isLiked = this.isLiked,
        )
    }

    fun fromCategoryItems(categoryItems: List<VideoCategoryItems>): List<CategoryItemModel>{
        return categoryItems.map{
            CategoryItemModel(
                id = it.id,
                title = it.snippet.title,
                channelId = it.snippet.channelId,
                assignable = it.snippet.assignable,
                isLiked = false, // 기본값 추가
            )
        }
    }

    fun fromChannelItems(channelItems: List<ChannelItem>): List<ChannelItemModel>{
        return channelItems.map {
            ChannelItemModel(
                id = it.id,
                title = it.snippet.title,
                description = it.snippet.description,
                channelThumbnails = it.snippet.thumbnails.high.url
            )
        }
    }
}