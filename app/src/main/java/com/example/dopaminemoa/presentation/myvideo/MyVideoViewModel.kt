package com.example.dopaminemoa.presentation.myvideo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.VideoRepository
import kotlinx.coroutines.launch

class MyVideoViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    // 좋아요 아이템들에 대한 MutableLiveData 선언
    private val _likedItems: MutableLiveData<List<VideoItemModel>> = MutableLiveData()

    // 외부에서 관찰할 수 있도록 LiveData로 제공
    val likedItems: LiveData<List<VideoItemModel>> get() = _likedItems

    // 저장된 좋아요 아이템들을 가져오는 함수
    fun getLikedItems() {
        viewModelScope.launch {
            _likedItems.value = videoRepository.getStorageItems()
        }
    }

    private fun saveVideoItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.saveVideoItem(videoItemModel.copy(isLiked = true))
        }
    }

    private fun removeVideoItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.removeVideoItem(videoItemModel.copy(isLiked = false))
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

    class MyVideoViewModelFactory(
        private val videoRepository: VideoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyVideoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyVideoViewModel(videoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}