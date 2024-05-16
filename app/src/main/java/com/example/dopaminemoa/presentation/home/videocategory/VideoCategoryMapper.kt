package com.example.dopaminemoa.presentation.home.videocategory

import com.example.dopaminemoa.data.eachResponse.VideoCategoryItems
import com.example.dopaminemoa.data.eachResponse.VideoCategoryListResponse
import com.example.dopaminemoa.data.eachResponse.VideoCategorySnippet

fun VideoCategoryListResponse.toEntity() = VideoCategoryEntity(
    items = items.asVideoCategoryItemsEntity()
)

fun List<VideoCategoryItems>.asVideoCategoryItemsEntity(): List<VideoCategoryItemsEntity> {
    return map {
        VideoCategoryItemsEntity(
            it.id,
            it.snippet.toEntity()
        )
    }
}

fun VideoCategorySnippet.toEntity(): VideoCategorySnippetEntity {
    return VideoCategorySnippetEntity(
        title,
//        channelId
    )
}