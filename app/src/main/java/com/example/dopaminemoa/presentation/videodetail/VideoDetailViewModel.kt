package com.example.dopaminemoa.presentation.videodetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.R
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.VideoRepository
import kotlinx.coroutines.launch

class VideoDetailViewModel (
    private val videoRepository: VideoRepository,
    private val applicationContext: Context

) : ViewModel() {

    private val _saveResult = MutableLiveData<SaveUiState>()
    val saveResult: LiveData<SaveUiState> get() = _saveResult

    private fun saveVideoItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.saveVideoItem(videoItemModel.copy(isLiked = true))
            _saveResult.value = SaveUiState.Success(applicationContext.getString(R.string.videodetail_snack_save))
        }
    }

    private fun removeVideoItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.removeVideoItem(videoItemModel.copy(isLiked = false))
            _saveResult.value = SaveUiState.Success(applicationContext.getString(R.string.videodetail_snack_delete))
        }
    }

    fun updateSaveItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            if (videoItemModel.isLiked) {
                saveVideoItem(videoItemModel)
            } else {
                removeVideoItem(videoItemModel)
            }
        }
    }

    fun isVideoLikedInPrefs(videoId: String?): Boolean {
        return videoRepository.isVideoLikedInPrefs(videoId)
    }

    fun clearPreferences() {
        videoRepository.clearPrefs()
    }
}

class VideoDetailViewModelFactory(
    private val videoRepository: VideoRepository,
    private val applicationContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoDetailViewModel(videoRepository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}