package com.example.dopaminemoa.presentation.myvideo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.MyvideoItemBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel

class MyVideoAdapter : RecyclerView.Adapter<MyVideoAdapter.MyVideoViewHolder>() {

    var itemClick: ItemClick? = null
    private val items: MutableList<VideoItemModel> = mutableListOf()

    fun updateList(newItems: List<VideoItemModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVideoViewHolder {
        val binding = MyvideoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyVideoViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyVideoViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, items[position])
        }
    }

    class MyVideoViewHolder(private val binding: MyvideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItemModel) = with(binding) {
            tvTitle.text = item.videoTitle
            tvChannelName.text = item.channelTitle
            Glide.with(itemView).load(item.videoThumbnail).into(ivThumbnail)
        }
    }

    interface ItemClick {
        fun onClick(view: View, item: VideoItemModel)
    }
}