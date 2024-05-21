package com.example.dopaminemoa.presentation.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.VideoRepository
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import kotlinx.coroutines.launch

class SearchViewModel(private val videoRepository: VideoRepository) : ViewModel() {
    private val _searchResults: MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val searchResults: LiveData<List<VideoItemModel>> get() = _searchResults
    private val _nextPageToken: MutableLiveData<String> = MutableLiveData()
    val nextPageToken: LiveData<String> get() = _nextPageToken

    private val _searchResultErrorState = MutableLiveData<Boolean>()
    val searchResultErrorState : LiveData<Boolean> get() = _searchResultErrorState
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> get() = _errorMessage


    /**
     * repository에 검색어를 사용한 검색 결과를 요청합니다.
     */
    fun searchVideoByText(text: String) = viewModelScope.launch {
        try {
            val (nextPageToken, videoItems) = videoRepository.searchVideoByText(text)
            _searchResults.value = videoItems

            _nextPageToken.value = nextPageToken
            _searchResultErrorState.value = false
        } catch (e: VideoRepositoryImpl.ApiException) {
            setErrorMessage(e.message)
            _searchResultErrorState.value = true
        }
    }

    /**
     * repository에 현재 사용중인 검색어에 대한 추가 데이터를 요청합니다.
     */
    fun searchMoreVideoByText(text: String, token: String) = viewModelScope.launch {
        try {
            val (nextPageToken, videoItems) = videoRepository.searchMoreVideoByText(text, token)
            val addItems = videoItems
            val currentList = _searchResults.value ?: emptyList()
            val updateList = currentList + addItems
            _searchResults.setValue(updateList)

            _nextPageToken.value = nextPageToken
            _searchResultErrorState.value = false
        } catch (e: VideoRepositoryImpl.ApiException) {
            setErrorMessage(e.message)
            _searchResultErrorState.value = true
        }
    }

    fun setErrorMessage(message: String?) {
        when {
            message?.contains("400") == true -> {
                _errorMessage.postValue("400 : 잘못된 요청입니다.")
            }
            message?.contains("401") == true -> {
                _errorMessage.postValue("401 : 요청이 승인되지 않았습니다.")
            }
            message?.contains("403") == true -> {
                _errorMessage.postValue("403 : 현재 기능을 일시적으로 사용할 수 없습니다.")
            }
            message?.contains("404") == true -> {
                _errorMessage.postValue("404 : 정보를 찾을 수 없습니다.")
            }
            else -> {
                _errorMessage.postValue("알 수 없는 문제가 생겼습니다.")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val remoteDataSource: RemoteDataSource, private val context: Context) : ViewModelProvider.Factory {
    companion object {
        fun newInstance(remoteDataSource: RemoteDataSource, context: Context): SearchViewModelFactory {
            return SearchViewModelFactory(remoteDataSource, context)
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(VideoRepositoryImpl(remoteDataSource, context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}