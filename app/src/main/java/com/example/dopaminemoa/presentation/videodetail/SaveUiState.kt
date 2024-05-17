package com.example.dopaminemoa.presentation.videodetail

import com.example.dopaminemoa.mapper.VideoItemModel

data class SaveUiState(
    val savedList: List<VideoItemModel>,
    val showSnackMessage: Boolean = false,
    val snackMessage: Int? = null
) {
    companion object {
        fun init() = SaveUiState(
            savedList = emptyList(),
            showSnackMessage = false,
            snackMessage = null
        )
    }
}