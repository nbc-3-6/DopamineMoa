package com.example.dopaminemoa.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemChannelListBinding
import com.example.dopaminemoa.mapper.model.ChannelItemModel

class ChannelsAdapter(private var channelItems: List<ChannelItemModel>) : RecyclerView.Adapter<ChannelsAdapter.ChannelViewHolder>() {
    class ChannelViewHolder(private val binding: ItemChannelListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChannelItemModel) {
            binding.apply {
                tvChannelId.text = item.title
                Glide.with(itemView)
                    .load(item.channelThumbnails)
                    .into(ivThumbnail)
            }
        }
    }
    fun submitList(newItems: List<ChannelItemModel>) {
        channelItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChannelListBinding.inflate(inflater, parent, false)
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channelItems[position])
    }

    override fun getItemCount(): Int = channelItems.size
}