package com.example.dopaminemoa.presentation.home.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemVideoListBinding
import com.example.dopaminemoa.mapper.PopularVideoItemModel


class MostPopularAdapter(private var videoItems: List<PopularVideoItemModel>) :
    RecyclerView.Adapter<MostPopularAdapter.MostPopularViewHolder>() {

    inner class MostPopularViewHolder(private val binding: ItemVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PopularVideoItemModel) {
            binding.apply {
                tvTitle.text = item.videoTitle
                Glide.with(itemView)
                    .load(item.videoThumbnail) // 썸네일 이미지 URL
                    .into(ivThumbnail)
            }
        }
    }

    fun updateItems(newItems: List<PopularVideoItemModel>) {
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