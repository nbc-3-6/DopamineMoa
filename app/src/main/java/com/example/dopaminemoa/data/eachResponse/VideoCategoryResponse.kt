package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

//todo 주석 제거
data class VideoCategoryListResponse(
    @SerializedName("items") val items: List<VideoCategoryItems> //필요
)

data class VideoCategoryItems(
    @SerializedName("id") val id: String, //필요 (카테고리 int값)
    @SerializedName("snippet") val snippet: VideoCategorySnippet
)

data class VideoCategorySnippet(
    @SerializedName("title") val title: String, //제목
    @SerializedName("channelId") val channelId: String // 필요3
//    @SerializedName("assignable") val assignable: Boolean, // ?
)