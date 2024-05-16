package com.example.dopaminemoa.presentation.home.video.model



data class MostPopularItemEntity(
    val items: List<VideoItemsEntity>,
)

data class VideoItemsEntity(
    val id: String,
    val snippet: VideoSnippetEntity,
//    val contentDetails: VideoContentDetailsEntity,
)

data class VideoSnippetEntity(
    val title: String, //제목
    val description: String, //설명 (detail에서 필요)
    val thumbnails: VideoThumbnailsEntity,
    val categoryId: String, //카테고리 int
    val channelId: String, //채널 아이디
)

data class VideoThumbnailsEntity(
    val standard: VideoThumbnailEntity,
)

data class VideoThumbnailEntity( //썸네일 하위
    val url: String, //이미지 url
    val width: Int, //크기
    val height: Int, //크기
)

//data class VideoContentDetailsEntity(
//    val duration: String //영상 시간 PT*M*S
//)