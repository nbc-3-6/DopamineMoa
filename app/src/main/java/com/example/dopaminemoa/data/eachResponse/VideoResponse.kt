package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("items") val items: List<VideoItems>,
)

data class VideoItems(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("id") val videoId: String,
    @SerializedName("snippet") val snippet: VideoSnippet,
    @SerializedName("Statistics") val statistics: VideoStatistics
)

data class VideoSnippet(
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("channelId") val channelId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnails") val thumbnails: Thumbnails,
    @SerializedName("channelTitle") val channelTitle: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("liveBroadcastContent") val liveBroadcastContent: String,
    @SerializedName("defaultLanguage") val defaultLanguage: String,
    @SerializedName("localized") val localized: Localized,
    @SerializedName("defaultAudioLanguage") val defaultAudioLanguage: String,
)

data class Thumbnails(
    @SerializedName("default") val default: VideoThumbnail,
    @SerializedName("medium") val medium: VideoThumbnail,
    @SerializedName("high") val high: VideoThumbnail,
    @SerializedName("standard") val standard: VideoThumbnail,
    @SerializedName("maxres") val maxres: VideoThumbnail,
)

data class VideoThumbnail(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)

data class Localized(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)

data class VideoStatistics(
    @SerializedName("viewCount") val viewCount: String,
    @SerializedName("likeCount") val likeCount: String,
    @SerializedName("dislikeCount") val dislikeCount: String,
    @SerializedName("favoriteCount") val favoriteCount: String,
    @SerializedName("commentCount") val commentCount: String,
)