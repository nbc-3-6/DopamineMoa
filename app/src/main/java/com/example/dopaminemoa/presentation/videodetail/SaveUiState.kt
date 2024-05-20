package com.example.dopaminemoa.presentation.videodetail

sealed class SaveUiState {
    object Init : SaveUiState()
    data class Success(val message: String) : SaveUiState()
    data class Error(val exception: Throwable) : SaveUiState()
}