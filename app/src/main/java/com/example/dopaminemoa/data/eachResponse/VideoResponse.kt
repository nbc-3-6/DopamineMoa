package com.example.dopaminemoa.data.eachResponse

import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("items") val items: List<VideoItems>,
)

data class VideoItems(
    @SerializedName("id") val id: String,
    @SerializedName("snippet") val snippet: VideoSnippet
)

data class VideoSnippet(
    @SerializedName("title") val title: String, //제목
    @SerializedName("description") val description: String, //설명 (detail에서 필요)
    @SerializedName("thumbnails") val thumbnails: VideoThumbnails, //썸네일 todo 시도 1.standard 사용?
)

data class VideoThumbnails(
    @SerializedName("standard") val standard: VideoThumbnail, //todo 시도1
)

data class VideoThumbnail( //썸네일 하위
    @SerializedName("url") val url: String, //이미지 url
    @SerializedName("width") val width: Int, //크기
    @SerializedName("height") val height: Int, //크기
)
