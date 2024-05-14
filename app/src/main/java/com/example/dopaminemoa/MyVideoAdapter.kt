package com.example.dopaminemoa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemoa.databinding.MyvideoItemBinding
import com.example.dopaminemoa.mapper.VideoItemModel
import de.hdodenhof.circleimageview.CircleImageView

class MyVideoAdapter(private val mContext: Context): RecyclerView.Adapter<MyVideoAdapter.ItemViewHolder>() {

    var items = ArrayList<VideoItemModel>()
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MyvideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

    }
    inner class ItemViewHolder(binding: MyvideoItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var iv_thum_image: ImageView = binding.ivThumbnail
        var tv_title: TextView = binding.tvTitle
        var iv_channel: CircleImageView= binding.ivChannel
        var cl_thumb_item: ConstraintLayout = binding.clThumbItem

        init {
            iv_thum_image.setOnClickListener(this)
            cl_thumb_item.setOnClickListener(this)
        }

        /**
         * 각 항목 클릭 시 발생하는 이벤트를 처리하는 메서드입니다.
         */
        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return


            notifyItemChanged(position)
        }
    }
}