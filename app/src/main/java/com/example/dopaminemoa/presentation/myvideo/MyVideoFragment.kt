package com.example.dopaminemoa.presentation.myvideo

import android.os.Bundle
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
        binding.rvMyVideo.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvMyVideo.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item =
            arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT)

        viewModel.getLikedItems() // 라이브 데이터 준비
        viewModel.likedItems.observe(viewLifecycleOwner) {
            adapter.itemClick = object : MyVideoAdapter.ItemClick {
                override fun onClick(view: View, item: VideoItemModel) {
                    clickItem(item)
                    adapter.updateList(it)
                }
            }
        }
        val items = viewModel.likedItems.value
        if (items != null) {
            adapter.updateList(items)
        }

        with(binding) {
            item?.let {
                viewModel.updateSaveItem(it)
            }
            btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun clickItem(item: VideoItemModel) {
        val bundle = Bundle().apply {
            putParcelable(BUNDLE_KEY_FOR_DETAIL_FRAGMENT, item)
        }
        val detailFragment = VideoDetailFragment.newInstance(bundle)
        (requireActivity() as MainActivity).showVideoDetailFragment(detailFragment)
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
