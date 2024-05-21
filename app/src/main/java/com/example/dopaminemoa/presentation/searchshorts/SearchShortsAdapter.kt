package com.example.dopaminemoa.presentation.searchshorts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.databinding.ItemLoadingBinding
import com.example.dopaminemoa.databinding.ShortsItemBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel

class SearchShortsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
        private const val SPAN_COUNT = 2
    }

    var itemClick: ItemClick? = null
    private val items: MutableList<VideoItemModel> = mutableListOf()

    fun addItems(newItems: List<VideoItemModel>) {
        val insertPosition = items.size
        val filteredNewItems = newItems.filter { newItem ->
            !items.any { it.videoId == newItem.videoId}
        }
        items.addAll(filteredNewItems)
        notifyItemRangeInserted(insertPosition, filteredNewItems.size)
    }

    fun showLoadingView() {
        items.add(VideoItemModel.EMPTY_ITEM)
        notifyItemInserted(items.size - 1)
    }

    fun deleteLoading() {
        val lastIndex = items.lastIndexOf(VideoItemModel.EMPTY_ITEM)
        if (lastIndex != -1) {
            items.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = ShortsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShortsViewHolder(binding)
            }
            else -> {
                val binding = ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == VideoItemModel.EMPTY_ITEM) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ShortsViewHolder) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                itemClick?.onClick(it, items[position])
            }
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.visibility = View.VISIBLE
        }
    }

    fun setupSpanSizeLookup(gridLayoutManager: GridLayoutManager) {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) == VIEW_TYPE_LOADING) {
                    SPAN_COUNT
                } else {
                    1
                }
            }
        }
    }

    class ShortsViewHolder(private val binding: ShortsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItemModel) = with(binding) {
            tvTitle.text = item.videoTitle
            Glide.with(itemView).load(item.videoThumbnail).into(ivItem)
        }
    }

    class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val progressBar: ProgressBar = binding.pbBar
    }

    interface ItemClick {
        fun onClick(view: View, item: VideoItemModel)
    }
}