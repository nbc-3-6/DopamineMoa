package com.example.dopaminemoa.presentation.videodetail

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentVideoDetailBinding
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.presentation.search.SearchResultFragment.Companion.BUNDLE_KEY_FOR_DETAIL_FRAGMENT
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import com.google.android.material.snackbar.Snackbar

class VideoDetailFragment : Fragment() {
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoDetailViewModel by viewModels {
        VideoDetailViewModelFactory(VideoRepositoryImpl(requireContext()),
            requireContext().applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_DETAIL_FRAGMENT)
        val isLikedInPrefs = viewModel.isVideoLikedInPrefs(item?.videoId)
        Log.d(TAG, "isLikedInPrefs: $isLikedInPrefs")

        // 초기 바인딩 시 isLikedInPrefs 값에 따라 ivLike 이미지 설정
        updateLikeButton(item, isLikedInPrefs)

        viewModel.saveResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SaveUiState.Success -> {
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        with(binding) {
            ivThumbnail.load(item?.videoThumbnail)
            ivChannelThumbnail.load(item?.channelThumbnails)
            tvChannelTitle.text = item?.channelTitle
            tvTitle.text = item?.videoTitle
            tvDescription.text = item?.videoDescription

            ivBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            ivLike.setOnClickListener {
                item?.let {
                    it.isLiked = !it.isLiked  // 좋아요 상태 토글
                    viewModel.updateSaveItem(it)  // ViewModel에서 업데이트
                    updateLikeButton(it, it.isLiked)  // UI 업데이트
                }
            }
        }
    }

    private fun updateLikeButton(item: VideoItemModel?, isLikedInPrefs: Boolean) {
        if (isLikedInPrefs) {
            binding.ivLike.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_liked))
        } else {
            binding.ivLike.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_like))
        }
        item?.isLiked = isLikedInPrefs
        Log.d(TAG, "I_Liked2: ${item?.isLiked}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(bundle: Bundle): VideoDetailFragment {
            return VideoDetailFragment().apply {
                arguments = bundle
            }
        }
    }
}