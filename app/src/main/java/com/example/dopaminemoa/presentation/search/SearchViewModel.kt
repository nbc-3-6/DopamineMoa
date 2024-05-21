package com.example.dopaminemoa.presentation.search

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

class SearchViewModel(private val videoRepository: VideoRepository) : ViewModel() {
    private val _searchResults: MutableLiveData<Resource<List<VideoItemModel>>> = MutableLiveData()
    val searchResults: LiveData<Resource<List<VideoItemModel>>> get() = _searchResults

    /**
     * repository에 검색어를 사용한 검색 결과를 요청합니다.
     */
    fun searchVideoByText(text: String) = viewModelScope.launch {
        _searchResults.value = videoRepository.searchVideoByText(text)
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