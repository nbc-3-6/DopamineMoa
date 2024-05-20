package com.example.dopaminemoa.presentation.myvideo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.VideoRepository
import kotlinx.coroutines.launch

class MyVideoViewModel(
    private val videoRepository: VideoRepository,
    context: Context
) : ViewModel() {


    // 좋아요 아이템들에 대한 MutableLiveData 선언
    private val _likedItems = MutableLiveData<VideoItemModel>()

    // 외부에서 관찰할 수 있도록 LiveData로 제공
    val likedItems: LiveData<VideoItemModel> get() = _likedItems

    // 저장된 좋아요 아이템들을 가져오는 함수
    private fun getLikedItems(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.saveVideoItem(videoItemModel.copy(isLiked = true))
        }
    }

    private fun deleteVideoItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            videoRepository.removeVideoItem(videoItemModel.copy(isLiked = false))
        }
    }

    fun updatelikedItem(videoItemModel: VideoItemModel) {
        viewModelScope.launch {
            if (videoItemModel.isLiked) {
                getLikedItems(videoItemModel)
            } else {
                deleteVideoItem(videoItemModel)
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

class MyVideoViewModelFactory(
    private val videoRepository: VideoRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyVideoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyVideoViewModel(videoRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}