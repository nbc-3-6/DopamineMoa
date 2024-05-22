package com.example.dopaminemoa.presentation.myvideo

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.MyvideoItemBinding
import com.example.dopaminemoa.databinding.SearchItemBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel

class MyVideoAdapter : RecyclerView.Adapter<MyVideoAdapter.MyVideoViewHolder>() {

    var itemClick: ItemClick? = null
    private val items: MutableList<VideoItemModel> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newItems: List<VideoItemModel>) {
        items.clear()
        items.addAll(newItems.reversed())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVideoViewHolder {
        val binding = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyVideoViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyVideoViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, items[position])
            notifyDataSetChanged()
        }
    }

    class MyVideoViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
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