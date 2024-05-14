package com.example.dopaminemoa.presentation.home.video.model

//todo duration을 받아서 60초 미만인 data 가져오기
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
    val thumbnails: VideoThumbnailsEntity, //썸네일 적절한 크기 todo 시도 1.standard 사용
)

data class VideoThumbnailsEntity(
    val standard: VideoThumbnailEntity, //todo 시도1
)

data class VideoThumbnailEntity( //썸네일 하위
    val url: String, //이미지 url
    val width: Int, //크기
    val height: Int, //크기
)

//data class VideoContentDetailsEntity(
//    val duration: String //영상 시간 PT*M*S
//)