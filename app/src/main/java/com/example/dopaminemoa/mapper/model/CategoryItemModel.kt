package com.example.dopaminemoa.mapper.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItemModel(
    val id: String,
    val title: String,
    val channelId: String,
    val assignable: Boolean,
    var isLiked: Boolean,
) : Parcelable