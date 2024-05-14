package com.example.dopaminemoa.mapper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoItemModel(
    var type: Int,
    val id: String,
    var isLike: Boolean,
    var title: String,
    var channel: String
) : Parcelable