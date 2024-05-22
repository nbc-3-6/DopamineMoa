package com.example.dopaminemoa.presentation

import android.view.View
import android.widget.Button
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.CustomPlayerUiBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

class CustomUiController(private val customPlayerUi: View, private val youTubePlayer: YouTubePlayer) {
//class CustomUiController(private val binding: CustomPlayerUiBinding, private val youTubePlayer: YouTubePlayer) {

    private var isPlaying = false

    init {
        val playPauseButton: Button = customPlayerUi.findViewById(R.id.play_pause_button)
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                youTubePlayer.pause()
            } else {
                youTubePlayer.play()
            }
            isPlaying = !isPlaying
        }
    }
}