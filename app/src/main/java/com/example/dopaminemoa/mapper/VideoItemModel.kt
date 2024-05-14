package com.example.dopaminemoa.mapper

import android.os.Parcelable
import com.example.dopaminemoa.data.eachResponse.SearchThumbnails
import com.example.dopaminemoa.data.eachResponse.VideoThumbnail
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoItemModel(
    val videoId: String,
    val videoTitle: String,
    val videoThumbnail: String,
    val videoViews: String,
    val videoDescription: String,
    val channelTitle: String,
    val channelThumbnails: String
) : Parcelable