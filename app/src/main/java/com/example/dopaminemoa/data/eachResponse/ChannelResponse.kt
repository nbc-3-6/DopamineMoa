package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class ChannelListResponse(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("pageInfo") val pageInfo: ChannelPageInfo,
    @SerializedName("items") val items: List<ChannelItem>
)

data class ChannelPageInfo(
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("resultsPerPage") val resultsPerPage: Int
)

data class ChannelItem(
    @SerializedName("kind") val kind: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("id") val id: String,
    @SerializedName("contentDetails") val contentDetails: ChannelContentDetails
)

data class ChannelContentDetails(
    @SerializedName("relatedPlaylists") val relatedPlaylists: ChannelRelatedPlaylists
)

data class ChannelRelatedPlaylists(
    @SerializedName("likes") val likes: String,
    @SerializedName("uploads") val uploads: String
)