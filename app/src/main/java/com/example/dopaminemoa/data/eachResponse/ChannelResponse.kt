package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class ChannelListResponse(
    @SerializedName("items") val items: List<ChannelItem>
)

data class ChannelItem(
    @SerializedName("id") val id: String,
    @SerializedName("snippet") val snippet: ChannelSnippet,
)

data class ChannelSnippet(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumbnails") val thumbnails: ChannelThumbnails,
)

data class ChannelThumbnails(
    @SerializedName("high") val high: ChannelThumbnail,
)

data class ChannelThumbnail(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)

