package com.example.dopaminemoa.mapper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelItemModel(
    val id: String,
    val title: String,
    val description: String,
    val channelThumbnails: String,
): Parcelable