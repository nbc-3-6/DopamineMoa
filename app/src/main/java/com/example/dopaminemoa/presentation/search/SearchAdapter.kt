package com.example.dopaminemoa.presentation.searchshorts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.SearchItemBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var itemClick: ItemClick? = null
    private val items: MutableList<VideoItemModel> = mutableListOf()

    fun updateList(newItems: List<VideoItemModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, items[position])
        }
    }

    class SearchViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItemModel) = with(binding) {
            tvTitle.text = item.videoTitle
            tvChannel.text = item.channelTitle
            Glide.with(itemView).load(item.videoThumbnail).into(ivItem)
        }
    }

    interface ItemClick {
        fun onClick(view: View, item: VideoItemModel)
    }
}