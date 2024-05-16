package com.example.dopaminemoa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.dopaminemoa.presentation.home.video.model.MostPopularItemEntity
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.network.RetrofitClient
import com.example.dopaminemoa.presentation.home.videocategory.model.VideoCategoryEntity
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import kotlinx.coroutines.launch


class VideoViewModel(private val videoRepositoryImpl: VideoRepositoryImpl) : ViewModel() {

    private val _popularResults: MutableLiveData<MostPopularItemEntity> = MutableLiveData()
    val popularResults: LiveData<MostPopularItemEntity> get() = _popularResults

    //카테고리만 받기
    private val _categoryVideoResults: MutableLiveData<VideoCategoryEntity> = MutableLiveData()
    val categoryVideoResults: LiveData<VideoCategoryEntity> get() = _categoryVideoResults

    //카테고리에 대한 영상
    private val _videoListByCategory: MutableLiveData<VideoCategoryEntity> = MutableLiveData()
    val videoListByCategory: LiveData<VideoCategoryEntity> get() = _videoListByCategory

    private val _categoryChannelResults: MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val categoryChannelResults: LiveData<List<VideoItemModel>> get() = _categoryChannelResults

    private val _searchResults: MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val searchResults: LiveData<List<VideoItemModel>> get() = _searchResults

    /**
     * repository에 인기 비디오 검색 결과를 요청합니다.
     */
    fun searchPopularVideo() = viewModelScope.launch {
        _popularResults.value = videoRepositoryImpl.searchPopularVideo()
    }

    /**
     * repository에 키워드를 사용한 비디오 검색 결과를 요청합니다.
     */
//    fun searchVideoByCategory(category: String) = viewModelScope.launch {
//        _videoListByCategory.value = videoRepositoryImpl.searchVideoByCategory(category)
//    }

    //spinner 카테고리
    fun takeVideoCategories() = viewModelScope.launch {
        _categoryVideoResults.value = videoRepositoryImpl.fetchVideoCategories()
    }


    /**
     * repository에 키워드를 사용한 채널 검색 결과를 요청합니다.
     */
    fun searchChannelByCategory(category: String) = viewModelScope.launch {
        _categoryChannelResults.value = videoRepositoryImpl.searchChannelByCategory(category)
    }

    /**
     * repository에 검색어를 사용한 검색 결과를 요청합니다.
     */
    fun searchVideoByText(text: String) = viewModelScope.launch {
        _searchResults.value = videoRepositoryImpl.searchVideoByText(text)
    }
}


/**
 * 하나의 viewmodel을 사용할 수 있도록 싱글톤으로 작성해놨습니다.
 * 필요한 fragment에서 아래와 같이 선언 후 사용하면 됩니다.
 * private val viewmodel : VideoViewModel by viewModels({ requireActivity() }) {
 *         VideoViewModelFactory.newInstance()
 *     }
 */
//@Suppress("UNCHECKED_CAST")
//class VideoViewModelFactory : ViewModelProvider.Factory {
//    companion object {
//        private val repository = VideoRepositoryImpl()
//
//        fun newInstance(): ViewModelProvider.Factory {
//            return object : ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
//                        return VideoViewModel(repository) as T
//                    }
//                    throw IllegalArgumentException("Unknown viewmodel class")
//                }
//            }
//        }
//    }
//}
class SearchViewModelFactory : ViewModelProvider.Factory {
    private val repository = VideoRepositoryImpl(RetrofitClient.youtubeService)
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        return VideoViewModel(repository) as T
    }
}