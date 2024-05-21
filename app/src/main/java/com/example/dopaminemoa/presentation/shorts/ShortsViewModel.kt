package com.example.dopaminemoa.presentation.shorts

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.repository.Resource
import com.example.dopaminemoa.repository.VideoRepository
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import kotlinx.coroutines.launch

class ShortsViewModel (private val videoRepository: VideoRepository) : ViewModel() {
    private val _searchResultsForShorts: MutableLiveData<List<VideoItemModel>> = MutableLiveData()
    val searchResultsForShorts: LiveData<List<VideoItemModel>> get() = _searchResultsForShorts
    private val _searchResultErrorState = MutableLiveData<Boolean>()
    val searchResultErrorState : LiveData<Boolean> get() = _searchResultErrorState
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> get() = _errorMessage

    /**
     * repository에 검색어를 사용한 검색 결과를 요청합니다.
     */
    fun searchVideoByTextForShorts(text: String) = viewModelScope.launch {
//        _searchResultsForShorts.value = videoRepository.searchVideoByText(text)
        try {
            _searchResultsForShorts.value = videoRepository.searchVideoByText(text)
            _searchResultErrorState.value = false
        } catch (e: VideoRepositoryImpl.ApiException) {
            setErrorMessage(e.message)
            _searchResultErrorState.value = true
        }
    }

    /**
     * repository에 현재 사용중인 검색어에 대한 추가 데이터를 요청합니다.
     */
    fun searchMoreVideoByTextForShorts(text: String, token: String) = viewModelScope.launch {
//        _searchResultsForShorts.value = videoRepository.searchMoreVideoByText(text, token)
//        val addItems = videoRepository.searchMoreVideoByText(text, token).data
        val currentList = _searchResultsForShorts.value
//        val updateList = currentList?.plus(addItems)
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
class ShortsViewModelFactory(private val remoteDataSource: RemoteDataSource, private val context: Context) : ViewModelProvider.Factory {
    companion object {
        fun newInstance(remoteDataSource: RemoteDataSource, context: Context): ShortsViewModelFactory {
            return ShortsViewModelFactory(remoteDataSource, context)
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShortsViewModel::class.java)) {
            return ShortsViewModel(VideoRepositoryImpl(remoteDataSource, context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}