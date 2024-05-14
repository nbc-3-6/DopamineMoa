package com.example.dopaminemoa.presentation.home.video.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemVideoListBinding
import com.example.dopaminemoa.presentation.home.video.model.VideoItemsEntity


class MostPopularAdapter(private var videoItems: List<VideoItemsEntity>) :
    RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder>() {

    inner class MostPopularViewHolder(private val binding: ItemVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(videoitem: VideoItemsEntity) {
            binding.apply {
                tvTitle.text = videoitem.snippet.title
                Glide.with(itemView)
                    .load(videoitem.snippet.thumbnails.standard.url) // 썸네일 이미지 URL
                    .into(ivThumbnail)
            }
        }
    }

    fun updateItems(newItems: List<VideoItemsEntity>) {
        videoItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoListBinding.inflate(inflater, parent, false)
        return MostPopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MostPopularViewHolder, position: Int) {
        holder.bind(videoItems[position])
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }
}