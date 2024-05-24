package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName


data class SearchListResponse(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("nextPageToken") val nextPageToken: String,
    @SerializedName("regionCode") val regionCode: String,
    @SerializedName("pageInfo") val pageInfo: SearchPageInfo,
    @SerializedName("items") val items: List<SearchItems>,
)

data class SearchPageInfo(
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("resultsPerPage") val resultsPerPage: Int,
)

data class SearchItems(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("id") val id: SearchId,
    @SerializedName("snippet") val snippet: SearchSnippet,
)

data class SearchId(
    @SerializedName("kind") val kind: String,
    @SerializedName("videoId") val videoId: String
)

data class SearchSnippet(
    @SerializedName("publishedAt") val publishedAt : String,
    @SerializedName("channelId") val channelId : String,
    @SerializedName("title") val title : String, // 제목
    @SerializedName("description") val description: String, // 내용
    @SerializedName("thumbnails") val thumbnails : SearchThumbnails, // 썸네일

    @SerializedName("channelTitle") val channelTitle: String,
    @SerializedName("liveBroadcastContent") val liveBroadcastContent: String,
    @SerializedName("publishTime") val publishTime:String,
)

data class SearchThumbnails(
    @SerializedName("high") val high: SearchThumbnail,
)

data class SearchThumbnail(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)