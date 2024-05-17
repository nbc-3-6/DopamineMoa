package com.example.dopaminemoa.presentation.videodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.R
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.repository.VideoRepository
import kotlinx.coroutines.launch

class VideoDetailViewModel (
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _saveResult = MutableLiveData(SaveUiState.init())
    val saveResult: LiveData<SaveUiState> get() = _saveResult
    private fun saveVideoItem(videoItemModel: VideoItemModel) = viewModelScope.launch {
        videoRepository.saveVideoItem(videoItemModel)

        updateSnackMessage(R.string.videodetail_snack_save)
    }

    private fun removeVideoItem(videoItemModel: VideoItemModel) = viewModelScope.launch {
        videoRepository.removeVideoItem(videoItemModel)

        updateSnackMessage(R.string.videodetail_snack_delete)
    }

    private fun updateSnackMessage(snackMessage: Int) {
        _saveResult.value = _saveResult.value?.copy(
            showSnackMessage = true,
            snackMessage = snackMessage
        )
    }

    fun updateSaveItem(videoItemModel: VideoItemModel) {
        val updatedItem = videoItemModel.copy(isLiked = videoItemModel.isLiked.not())

        viewModelScope.launch {
            if (updatedItem.isLiked) {
                saveVideoItem(updatedItem)
            } else {
                removeVideoItem(updatedItem)
            }

            // 좋아요 상태가 변경된 후, _saveResult의 list를 업데이트
            _saveResult.value = _saveResult.value?.copy(
                savedList = _saveResult.value?.savedList?.map {
                    if (it.videoId == updatedItem.videoId) updatedItem else it
                } ?: emptyList()
            )
        }
    }

    fun reloadStorageItems() = viewModelScope.launch {
        val storageItems = videoRepository.getStorageItems()

        _saveResult.value = _saveResult.value?.copy(
            showSnackMessage = false,
            savedList = _saveResult.value?.savedList?.map { currentItem ->
                currentItem.copy(isLiked = storageItems.any { it.videoId == currentItem.videoId })
            } ?: emptyList()
        )
    }
}

class VideoDetailViewModelFactory(
    private val videoRepository: VideoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VideoDetailViewModel(videoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}