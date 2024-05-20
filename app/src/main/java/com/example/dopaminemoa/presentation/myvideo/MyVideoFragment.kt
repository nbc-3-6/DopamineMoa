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
import com.example.dopaminemoa.presentation.shorts.ShortsFragment
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment
import com.example.dopaminemoa.repository.VideoRepositoryImpl

class MyVideoFragment : Fragment() {

    private var _binding: FragmentMyVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MyVideoAdapter

    private val viewModel: MyVideoViewModel by viewModels {
        MyVideoViewModelFactory(
            VideoRepositoryImpl(RepositoryClient.youtubeService, requireContext()),
            requireContext().applicationContext
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = MyVideoAdapter()
        _binding = FragmentMyVideoBinding.inflate(inflater, container, false).apply {
            rvMyVideo.layoutManager = GridLayoutManager(requireActivity(), 2)
            rvMyVideo.adapter = adapter
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.likedItems.observe(viewLifecycleOwner) { videoItems ->
            // 데이터가 변경될 때마다 RecyclerView에 새로운 데이터를 설정합니다.
            binding.rvMyVideo.adapter = MyVideoAdapter()
        }

        val item =
            arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT)




        with(binding) {
            item?.let {
//                tvTitle.text = item?.videoTitle
            }
            btnBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    // 프래그먼트 뷰 종료 시 호출
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 바인딩 리소스 해제
    }

    companion object {
        const val BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT = "BUNDLE_KEY_FOR_MYVIDEO_FRAGMENT"

//        fun newInstance(bundle: Bundle): MyVideoFragment {
//            return MyVideoFragment().apply {
//                arguments = bundle
//            }
//        }

        fun newInstance(): MyVideoFragment {
            return MyVideoFragment()

        }
    }
}
