package com.example.dopaminemoa.presentation.myvideo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dopaminemoa.databinding.FragmentMyVideoBinding
import com.example.dopaminemoa.mapper.VideoItemModel

class MyVideoFragment : Fragment() {

    private var binding: FragmentMyVideoBinding? = null
    private lateinit var adapter: MyVideoAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager
    private lateinit var mContext: Context

//    val sharedViewModel by activityViewModels<SharedViewModel>()
    private val viewModel: MyVideoViewModel by viewModels()

    companion object {
        fun newInstance() = MyVideoFragment()
    }

    // 프래그먼트가 액티비티에 붙을 때 호출
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 어댑터 초기화
        adapter = MyVideoAdapter(mContext)

        // 바인딩 설정
        binding = FragmentMyVideoBinding.inflate(inflater, container, false).apply {
            rvMyVideo.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvMyVideo.adapter = adapter
            rvMyVideo.visibility = View.GONE
        }

        // 좋아요된 아이템 로딩
        viewModel.getLikedItems(mContext)

        // 좋아요 리스트 관찰하여 UI 업데이트
        viewModel.likedItems.observe(viewLifecycleOwner) { likes ->
            adapter.items = likes.toMutableList() as ArrayList<VideoItemModel>
            adapter.notifyDataSetChanged()
        }

        // 항목 클릭 시 동작 정의
        adapter.setOnItemClickListener(object : MyVideoAdapter.OnItemClickListener {
            override fun onItemClick(item: VideoItemModel, position: Int) {
                viewModel.deleteItem(mContext, item, position)
                Log.d("MyVideoFragment", "#dopamine onItemClick deleteItem position = $position")
//                sharedViewModel.addDeletedItemUrls(item.id)

            }
        })
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnBack?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            Log.d("되냐?", binding?.btnBack.toString())
        }
    }
    // 프래그먼트 뷰 종료 시 호출
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null  // 바인딩 리소스 해제
    }
}