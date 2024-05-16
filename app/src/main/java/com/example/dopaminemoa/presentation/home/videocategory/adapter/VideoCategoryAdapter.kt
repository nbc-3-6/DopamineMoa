package com.example.dopaminemoa.presentation.home.videocategory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemVideoListBinding
import com.example.dopaminemoa.presentation.home.video.model.VideoItemsEntity

class VideoCategoryAdapter(private var videoItems: List<VideoItemsEntity>) :
    RecyclerView.Adapter<VideoCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemVideoListBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoListBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(videoItems[position])
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }
}