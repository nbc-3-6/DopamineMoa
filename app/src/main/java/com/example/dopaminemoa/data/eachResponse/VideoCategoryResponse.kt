package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class VideoCategoryListResponse(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("items") val items: List<VideoCategoryItems>
)

data class VideoCategoryItems(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("id") val id: String,
    @SerializedName("snippet") val snippet: VideoCategorySnippet
)

data class VideoCategorySnippet(
    @SerializedName("title") val title: String,
    @SerializedName("assignable") val assignable: Boolean,
    @SerializedName("channelId") val channelId: String
)