package com.example.dopaminemoa.presentation.myvideo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dopaminemoa.data.remote.RemoteDataSource
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.utils.Utils

class MyVideoViewModel : ViewModel() {

    // 좋아요 아이템들에 대한 MutableLiveData 선언
    private val _likedItems = MutableLiveData<List<VideoItemModel>>()

    // 외부에서 관찰할 수 있도록 LiveData로 제공
    val likedItems: LiveData<List<VideoItemModel>> get() = _likedItems

    // 저장된 좋아요 아이템들을 가져오는 함수
    fun getLikedItems(context: Context) {
        // Utils 클래스를 이용해 저장된 좋아요를 가져와서 _likedItems에 저장
        _likedItems.value = Utils.getPrefLikeItems(context)
    }
    // 특정 아이템을 삭제하는 함수
    fun deleteItem(context: Context, item: VideoItemModel, position: Int) {
        // Utils 클래스를 이용해 아이템 삭제
        Utils.deletePrefItem(context, item.videoId)
        Log.d("VideoItemModel", "dopaminemoa deleteItem position=${position}, id = ${item.videoId}")

        // 삭제된 아이템 정보를 반영하여 LiveData 업데이트
        _likedItems.value?.let { currentItems ->
            val updatedItems = currentItems.toMutableList()
            updatedItems.remove(item)
            _likedItems.value = updatedItems
        }
    }

}