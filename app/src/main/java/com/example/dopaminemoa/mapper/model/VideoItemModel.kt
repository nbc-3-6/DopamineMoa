package com.example.dopaminemoa.mapper.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoItemModel(
    @SerializedName("id") val videoId: String,
    val videoTitle: String,
    val videoThumbnail: String,
    val videoDescription: String,
    val channelTitle: String,
    val category: String,
    val channelId: String,
    var isLiked: Boolean,
) : Parcelable