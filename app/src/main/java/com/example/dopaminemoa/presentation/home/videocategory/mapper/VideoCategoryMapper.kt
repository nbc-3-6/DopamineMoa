package com.example.dopaminemoa.presentation.home.videocategory.mapper

import com.example.dopaminemoa.data.eachResponse.VideoCategoryItems
import com.example.dopaminemoa.data.eachResponse.VideoCategoryListResponse
import com.example.dopaminemoa.data.eachResponse.VideoCategorySnippet
import com.example.dopaminemoa.presentation.home.videocategory.model.VideoCategoryEntity
import com.example.dopaminemoa.presentation.home.videocategory.model.VideoCategoryItemsEntity
import com.example.dopaminemoa.presentation.home.videocategory.model.VideoCategorySnippetEntity

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