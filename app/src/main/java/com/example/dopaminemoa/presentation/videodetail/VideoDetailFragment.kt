package com.example.dopaminemoa.presentation.videodetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.dopaminemoa.Const.Companion.SHARE_URL
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentVideoDetailBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.example.dopaminemoa.presentation.shorts.ShortsFragment
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class VideoDetailFragment : Fragment() {
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoDetailViewModel by viewModels {
        VideoDetailViewModelFactory(
            VideoRepositoryImpl(RepositoryClient.youtubeService, requireContext()),
            requireContext().applicationContext
        )
    }

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var playerTracker: YouTubePlayerTracker
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val item = arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_DETAIL_FRAGMENT)
            ?: arguments?.getParcelable<VideoItemModel>(ShortsFragment.BUNDLE_KEY_FOR_DETAIL_FRAGMENT_FROM_SHORTS)
        val isLikedInPrefs = viewModel.isVideoLikedInPrefs(item?.videoId)

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
//            ivThumbnail.load(item?.videoThumbnail)
            tvChannelTitle.text = item?.channelTitle
            tvTitle.text = item?.videoTitle
            tvDescription.text = item?.videoDescription

            ivBack.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }

            llLike.setOnClickListener {
                item?.let {
                    it.isLiked = !it.isLiked
                    viewModel.updateSaveItem(it)
                    updateLikeButton(it, it.isLiked)
                }
            }

            llShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, SHARE_URL + item?.videoId)
                }
                startActivity(Intent.createChooser(shareIntent, "Share Video Link"))
            }
        }
        item?.videoId?.let { setUpYoutubePlayer(it) }
    }

    private fun setUpYoutubePlayer(videoId: String) {
        youTubePlayerView = binding.youtubePlayerView
        youTubePlayerView.enableAutomaticInitialization = false //초기화 수동
        lifecycle.addObserver(youTubePlayerView)

        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                playerTracker = YouTubePlayerTracker()
                youTubePlayer.addListener(playerTracker)

                youTubePlayer.loadVideo(videoId, 0f)
            }
        }
        val options = IFramePlayerOptions
            .Builder()
            .controls(0)
            .build()
        youTubePlayerView.initialize(listener, options)
    }


    private fun updateLikeButton(item: VideoItemModel?, isLikedInPrefs: Boolean) {
        binding.ivLike.setImageResource(
            if (isLikedInPrefs) R.drawable.ic_liked
            else R.drawable.ic_like
        )
        item?.isLiked = isLikedInPrefs
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        youTubePlayerView.release()
        // 변경된 좋아요 상태를 전달
        setFragmentResult("video_detail_result", Bundle().apply {
            putBoolean("is_liked_changed", true)
        })
    }

    companion object {
        const val BUNDLE_KEY_FOR_DETAIL_FRAGMENT = "BUNDLE_KEY_FOR_DETAIL_FRAGMENT"

        fun newInstance(bundle: Bundle): VideoDetailFragment {
            return VideoDetailFragment().apply {
                arguments = bundle
            }
        }
    }
}