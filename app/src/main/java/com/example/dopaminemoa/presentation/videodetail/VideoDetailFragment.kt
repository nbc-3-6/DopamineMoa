package com.example.dopaminemoa.presentation.videodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentVideoDetailBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.example.dopaminemoa.presentation.shorts.ShortsFragment
import com.example.dopaminemoa.repository.VideoRepositoryImpl
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
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

        val item = arguments?.getParcelable<VideoItemModel>(BUNDLE_KEY_FOR_DETAIL_FRAGMENT)
            ?: arguments?.getParcelable<VideoItemModel>(ShortsFragment.BUNDLE_KEY_FOR_DETAIL_FRAGMENT_FROM_SHORTS)

        val isLikedInPrefs = viewModel.isVideoLikedInPrefs(item?.videoId)

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
//            ivThumbnail.load(item?.videoThumbnail)
//            item?.videoId?.let { setUpYoutubePlayer(it) }
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
        item?.videoId?.let { setUpYoutubePlayer(it) }
    }

    private fun setUpYoutubePlayer(videoId: String){

        youTubePlayerView = binding.youtubePlayerVideo
        youTubePlayerView.enableAutomaticInitialization = false //초기화 수동
        lifecycle.addObserver(youTubePlayerView)

        val youTubePlayerListener = object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {}
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {}
            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {}
            override fun onPlaybackQualityChange(youTubePlayer: YouTubePlayer, playbackQuality: PlayerConstants.PlaybackQuality) {}
            override fun onPlaybackRateChange(youTubePlayer: YouTubePlayer, playbackRate: PlayerConstants.PlaybackRate) {}
            override fun onReady(youTubePlayer: YouTubePlayer) {}
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {}
            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {}
            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {}
            override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {}
        }
        youTubePlayerView.removeYouTubePlayerListener(youTubePlayerListener)

        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                playerTracker = YouTubePlayerTracker()
                youTubePlayer.addListener(playerTracker)

//                CustomUiController(customPlayerUi, youTubePlayer)
                youTubePlayer.loadVideo(videoId, 0f)
            }
        }

        val options = IFramePlayerOptions.Builder().controls(0).build()
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