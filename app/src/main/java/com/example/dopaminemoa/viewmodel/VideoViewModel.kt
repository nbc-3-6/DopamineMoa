package com.example.dopaminemoa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.model.PopularVideoItemModel
import com.example.dopaminemoa.mapper.model.CategoryItemModel
import com.example.dopaminemoa.mapper.model.ChannelItemModel
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.Resource
import com.example.dopaminemoa.repository.VideoRepository
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import kotlinx.coroutines.launch

class VideoViewModel(private val videoRepository: VideoRepository) : ViewModel() {

    private val _popularResults: MutableLiveData<List<PopularVideoItemModel>> = MutableLiveData()
    val popularResults: LiveData<List<PopularVideoItemModel>> get() = _popularResults

    //카테고리만 받기
    private val _categoryVideoResults: MutableLiveData<List<CategoryItemModel>> = MutableLiveData()
    val categoryVideoResults: LiveData<List<CategoryItemModel>> get() = _categoryVideoResults

    //카테고리에 대한 영상
    private val _videoListByCategory: MutableLiveData<List<PopularVideoItemModel>> = MutableLiveData()
    val videoListByCategory: LiveData<List<PopularVideoItemModel>> get() = _videoListByCategory

    //카테고리 video 데이터 중 channelId를 저장
    private val _channelIds: MutableLiveData<List<String>> = MutableLiveData()
    val channelIds: LiveData<List<String>> get() = _channelIds

    //저장된 channelId의 채널 리스트
    private val _categoryChannelResults: MutableLiveData<List<ChannelItemModel>> = MutableLiveData()
    val categoryChannelResults: LiveData<List<ChannelItemModel>> get() = _categoryChannelResults

    //카테고리 변동될때 같이 바뀌는 textView
    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> get() = _selectedCategory

    private val _searchResults: MutableLiveData<Resource<List<VideoItemModel>>> = MutableLiveData()
    val searchResults: LiveData<Resource<List<VideoItemModel>>> get() = _searchResults

    private val _searchResultsForShorts: MutableLiveData<Resource<List<VideoItemModel>>> = MutableLiveData()
    val searchResultsForShorts: LiveData<Resource<List<VideoItemModel>>> get() = _searchResultsForShorts

    //error전달
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    //error 확인
    private val _mostPopularErrorState = MutableLiveData<Boolean>()
    val mostPopularErrorState: LiveData<Boolean> get() = _mostPopularErrorState

    private val _categoryErrorState = MutableLiveData<Boolean>()
    val categoryErrorState: LiveData<Boolean> get() = _categoryErrorState

    /**
     * repository에 인기 비디오 검색 결과를 요청합니다.
     */
    fun searchPopularVideo() = viewModelScope.launch {
        try {
            _popularResults.value = videoRepository.searchPopularVideo()
            _mostPopularErrorState.value = false
        } catch (e: VideoRepositoryImpl.ApiException) {
            _errorMessage.postValue(e.message)
            _mostPopularErrorState.value = true
        }
    }

    /**
     * repository에 키워드를 사용한 비디오 검색 결과를 요청합니다.
     */
    fun searchVideoByCategory(category: String) = viewModelScope.launch {
        try {
            val videos = videoRepository.searchVideoByCategory(category)
            _videoListByCategory.value = videos
            _channelIds.value = videos.map { it.channelId } // channelId 저장
            _categoryErrorState.value = false
        } catch (e: VideoRepositoryImpl.ApiException) {
            _errorMessage.postValue(e.message)
            _categoryErrorState.value = true
        }
    }

    //spinner 카테고리 목록
    fun takeVideoCategories() = viewModelScope.launch {
        try {
            val allCategories = videoRepository.takeVideoCategories()
            val assignableCategories = allCategories.filter { it.assignable }
            _categoryVideoResults.value = assignableCategories
        } catch (e: VideoRepositoryImpl.ApiException) {
            _errorMessage.postValue(e.message)
        }
    }

    fun updateSelectedCategory(category: String) {
        try {
            _selectedCategory.value = category
        } catch (e: VideoRepositoryImpl.ApiException) {
            _errorMessage.postValue(e.message)
        }
    }

    /**
     * repository에 키워드를 사용한 채널 검색 결과를 요청합니다.
     */
    fun searchChannelByCategory(channel: List<String>) = viewModelScope.launch {
        try {
            _categoryChannelResults.value = videoRepository.searchChannelByCategory(channel.joinToString(","))
        } catch (e: VideoRepositoryImpl.ApiException) {
            _errorMessage.postValue(e.message)
        }
    }

    /**
     * repository에 검색어를 사용한 검색 결과를 요청합니다.
     */
    fun searchVideoByText(text: String) = viewModelScope.launch {
        _searchResults.value = videoRepository.searchVideoByText(text)
    }

    fun searchVideoByTextForShorts(text: String) = viewModelScope.launch {
        _searchResultsForShorts.value = videoRepository.searchVideoByText(text)
    }
}

@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory(
    private val remoteDataSource: RemoteDataSource,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(VideoRepositoryImpl(remoteDataSource, context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}