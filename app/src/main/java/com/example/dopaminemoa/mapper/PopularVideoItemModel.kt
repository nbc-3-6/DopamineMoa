package com.example.dopaminemoa.mapper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopularVideoItemModel(
    val videoId: String,
    val videoTitle: String,
    val videoThumbnail: String,
    val videoViews: String, // 나중에 추가
    val videoDescription: String,
    val channelTitle: String,
    val channelThumbnails: String,
    val categoryId: String,
//    val channelId: String,
) : Parcelable