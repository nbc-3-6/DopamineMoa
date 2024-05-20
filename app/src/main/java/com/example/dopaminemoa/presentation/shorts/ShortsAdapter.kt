package com.example.dopaminemoa.presentation.shorts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ShortsItemBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel

class ShortsAdapter : RecyclerView.Adapter<ShortsAdapter.ShortsViewHolder>() {

    var itemClick: ItemClick? = null
    private val items: MutableList<VideoItemModel> = mutableListOf()

    fun updateList(newItems: List<VideoItemModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder {
        val binding = ShortsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShortsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, items[position])
        }
    }

    class ShortsViewHolder(private val binding: ShortsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItemModel) = with(binding) {
            tvTitle.text = item.videoTitle
            Glide.with(itemView).load(item.videoThumbnail).into(ivItem)
        }
    }

    interface ItemClick {
        fun onClick(view: View, item: VideoItemModel)
    }
}