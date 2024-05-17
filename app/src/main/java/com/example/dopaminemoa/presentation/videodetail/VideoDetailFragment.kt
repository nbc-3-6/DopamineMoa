package com.example.dopaminemoa.presentation.videodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        VideoDetailViewModelFactory(VideoRepositoryImpl(requireContext()))
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
                    viewModel.updateSaveItem(it)
                }
            }
        }

        item?.let { videoItem ->
            viewModel.saveResult.observe(viewLifecycleOwner) { saveUiState ->
                saveUiState?.let {
                    if (it.showSnackMessage) {
                        it.snackMessage?.let { resId ->
                            showSnackBar(resId)
                        }
                    }

                    val isLiked = it.savedList.any { savedItem ->
                        savedItem.videoId == videoItem.videoId
                    }
                    binding.ivLike.setImageResource(
                        if (isLiked) R.drawable.ic_liked
                        else R.drawable.ic_like
                    )
                }
            }
        }

        viewModel.reloadStorageItems()
    }

    private fun showSnackBar(resId: Int) {
        Snackbar.make(binding.root, getString(resId), Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadStorageItems()
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