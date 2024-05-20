package com.example.dopaminemoa.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemVideoListBinding
import com.example.dopaminemoa.mapper.model.PopularVideoItemModel

class VideoCategoriesAdapter(
    private var videoItems: List<PopularVideoItemModel>,
    private val itemClickListener: (PopularVideoItemModel) -> Unit
) : RecyclerView.Adapter<VideoCategoriesAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemVideoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PopularVideoItemModel) {
            binding.apply {
                tvTitle.text = item.videoTitle
                Glide.with(itemView)
                    .load(item.videoThumbnail)
                    .into(ivThumbnail)

                root.setOnClickListener {
                    itemClickListener(item)
                }
            }
        }
    }

    fun submitList(newItems: List<PopularVideoItemModel>) {
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