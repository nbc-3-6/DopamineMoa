package com.example.dopaminemoa.presentation.myvideo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.example.dopaminemoa.databinding.FragmentMyVideoBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.example.dopaminemoa.presentation.main.MainActivity
import com.example.dopaminemoa.presentation.shorts.ShortsFragment
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment.Companion.BUNDLE_KEY_FOR_DETAIL_FRAGMENT
import com.example.dopaminemoa.repository.VideoRepositoryImpl

class MyVideoFragment : Fragment() {

    private var _binding: FragmentMyVideoBinding? = null
    private val binding get() = _binding!!

    private var adapter =  MyVideoAdapter()

    private val viewModel: MyVideoViewModel by viewModels {
        MyVideoViewModel.MyVideoViewModelFactory(
            VideoRepositoryImpl(RepositoryClient.youtubeService, requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        adapter = MyVideoAdapter()
        _binding = FragmentMyVideoBinding.inflate(inflater, container, false)
        binding.rvMyVideo.layoutManager = GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMyVideo.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT)
        setUpView()
        itemClick()
    }

    fun setUpView() {
        // 1, 2번은 거의 동시에 실행됨
        viewModel.getLikedItems() // 1. livedata 준비
        Log.d("1-getLikedItems()", viewModel.getLikedItems().toString())
        val items = viewModel.likedItems.value
        if (items != null) {
            adapter.updateList(items) // 2. 변화가 있을때 변경해줄 준비
            Log.d("2 - adapter.updateList(items)", adapter.updateList(items).toString())
        }
    }

    fun itemClick() {
        // 4번도 있었는데 2번 코드랑 같고, 아이템 클릭처리해서 디테일가는건데 뭐하러 view의 변화를 update로 해결해야하나 싶어서
        // 주석해봤더니 괜찮아서 지웠음
        viewModel.likedItems.observe(viewLifecycleOwner) {
            adapter.itemClick = object : MyVideoAdapter.ItemClick {
                override fun onClick(view: View, item: VideoItemModel) {
                    item?.let {
                        clickItem(item) // 3. 클릭했을 때, 디테일로 가기
                        Log.d("3 - clickItem(item)", clickItem(item).toString())
                        viewModel.updateSaveItem(it)  // ViewModel에서 업데이트
                        Log.d("4 - viewModel.updateSaveItem(it)", viewModel.updateSaveItem(it).toString())
                    }
                }
            }
        }
    }

    private var isItemClickEnabled = true

    private fun clickItem(item: VideoItemModel) {
        if (!isItemClickEnabled) return
        isItemClickEnabled = false

        val bundle = Bundle().apply {
            putParcelable(BUNDLE_KEY_FOR_DETAIL_FRAGMENT, item)
        }
        val detailFragment = VideoDetailFragment.newInstance(bundle)
        (requireActivity() as MainActivity).showVideoDetailFragment(detailFragment)

        Handler(Looper.getMainLooper()).postDelayed({
            isItemClickEnabled = true
        }, 500) // 500ms 딜레이
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 바인딩 리소스 해제
    }

    companion object {
        const val BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT = "BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT"
        fun newInstance() = MyVideoFragment()
    }
}
