package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class VideoCategoryListResponse(
    @SerializedName("items") val items: List<VideoCategoryItems>
)

data class VideoCategoryItems(
    @SerializedName("id") val id: String,
    @SerializedName("snippet") val snippet: VideoCategorySnippet
)

data class VideoCategorySnippet(
    @SerializedName("title") val title: String,
    @SerializedName("channelId") val channelId: String,
    @SerializedName("assignable") val assignable: Boolean,
)