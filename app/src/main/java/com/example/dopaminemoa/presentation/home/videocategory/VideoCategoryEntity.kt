package com.example.dopaminemoa.presentation.home.videocategory

data class VideoCategoryEntity(
    val items: List<VideoCategoryItemsEntity>
)

data class VideoCategoryItemsEntity(
    val id: String, //필요 (int값)
    val snippet: VideoCategorySnippetEntity
)

data class VideoCategorySnippetEntity(
    val title: String, // 카테고리 목록
//    val channelId: String // 필요3
)