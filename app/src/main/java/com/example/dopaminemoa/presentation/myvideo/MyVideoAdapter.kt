package com.example.dopaminemoa.presentation.myvideo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dopaminemoa.Constants
import com.example.dopaminemoa.databinding.MyvideoItemBinding
import com.example.dopaminemoa.mapper.VideoItemModel
import de.hdodenhof.circleimageview.CircleImageView

class MyVideoAdapter(private val mContext: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 표시될 항목들
    var items = ArrayList<VideoItemModel>()

    // 항목 클릭 리스너 인터페이스
    interface OnItemClickListener {
        fun onItemClick(item: VideoItemModel, position: Int)
    }

    private var clickListener: OnItemClickListener? = null

    // 클릭 리스너 설정
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MyvideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext)
            .load(items[position].id)
            .into((holder as ItemViewHolder).iv_thum_image)

        // 항목 타입 설정 (이미지 or 비디오)
        var type = "[Image] "
        if (items[position].type == Constants.SEARCH_TYPE_VIDEO) type = "[Video] "

        holder.tv_title.text = type + items[position].title

        // 항목 클릭 이벤트
        holder.cl_thumb_item.setOnClickListener {
            Log.d("MyVideoAdapter","#dopamine itemView onItemClick=${position}")
            clickListener?.onItemClick(items[position], position)
        }

    }

    // 항목에 데이터 바인딩
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