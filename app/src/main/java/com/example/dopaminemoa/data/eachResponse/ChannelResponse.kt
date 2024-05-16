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
    @SerializedName("snippet") val snippet: ChannelSnippet,
    @SerializedName("contentDetails") val contentDetails: ChannelContentDetails
)

data class ChannelSnippet(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("customUrl") val customUrl: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("thumbnails") val thumbnails: ChannelThumbnail,
    @SerializedName("defaultLanguage") val defaultLanguage: String,
    @SerializedName("localized") val localized: ChannelLocalized,
    @SerializedName("country") val country: String,
)

data class ChannelThumbnail(
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
)

data class ChannelLocalized(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)

data class ChannelContentDetails(
    @SerializedName("relatedPlaylists") val relatedPlaylists: ChannelRelatedPlaylists
)

data class ChannelRelatedPlaylists(
    @SerializedName("likes") val likes: String,
    @SerializedName("uploads") val uploads: String
)

