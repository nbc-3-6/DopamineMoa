package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("items") val items: List<VideoItems>,
)

data class VideoItems(
    @SerializedName("id") val videoId: String, //기존 id -> videoId
    @SerializedName("snippet") val snippet: VideoSnippet,
)

data class VideoSnippet(
    @SerializedName("title") val videoTitle: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnails") val thumbnails: VideoThumbnails,
    @SerializedName("channelTitle") val channelTitle: String,
    @SerializedName("categoryId") val category: String,
    @SerializedName("channelId") val channelId: String,
)

data class VideoThumbnails(
    @SerializedName("high") val high: VideoThumbnail,
)

data class VideoThumbnail(
    //썸네일 하위
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)

