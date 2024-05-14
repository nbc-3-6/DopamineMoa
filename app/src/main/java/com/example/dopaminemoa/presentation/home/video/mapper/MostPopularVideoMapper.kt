package com.example.dopaminemoa.presentation.home.video.mapper

import com.example.dopaminemoa.data.eachResponse.VideoItems
import com.example.dopaminemoa.data.eachResponse.VideoListResponse
import com.example.dopaminemoa.data.eachResponse.VideoSnippet
import com.example.dopaminemoa.data.eachResponse.VideoThumbnail
import com.example.dopaminemoa.data.eachResponse.VideoThumbnails
import com.example.dopaminemoa.presentation.home.video.model.MostPopularItemEntity
import com.example.dopaminemoa.presentation.home.video.model.VideoItemsEntity
import com.example.dopaminemoa.presentation.home.video.model.VideoSnippetEntity
import com.example.dopaminemoa.presentation.home.video.model.VideoThumbnailEntity
import com.example.dopaminemoa.presentation.home.video.model.VideoThumbnailsEntity


fun VideoListResponse.toEntity() = MostPopularItemEntity(
    items = items.asVideoItemsEntity()
)

fun List<VideoItems>.asVideoItemsEntity(): List<VideoItemsEntity> {
    return map {
        VideoItemsEntity(
            it.id,
            it.snippet.toEntity(),
//            it.contentDetails.toEntity() //추가
        )
    }
}

fun VideoSnippet.toEntity(): VideoSnippetEntity {
    return VideoSnippetEntity(
        title,
        description,
        thumbnails.toEntity()
    )
}

fun VideoThumbnails.toEntity(): VideoThumbnailsEntity {
    return VideoThumbnailsEntity(
        standard.toEntity()
    )
}

fun VideoThumbnail.toEntity(): VideoThumbnailEntity {
    return VideoThumbnailEntity(
        url,
        width,
        height
    )
}

//fun VideoContentDetails.toEntity(): VideoContentDetailsEntity{
//    return VideoContentDetailsEntity(
//        duration
//    )
//}