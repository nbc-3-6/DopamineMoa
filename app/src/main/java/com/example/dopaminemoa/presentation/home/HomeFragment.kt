package com.example.dopaminemoa.presentation.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemoa.databinding.FragmentHomeBinding
import com.example.dopaminemoa.presentation.home.video.adapter.MostPopularAdapter
import com.example.dopaminemoa.viewmodel.SearchViewModelFactory
import com.example.dopaminemoa.viewmodel.VideoViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VideoViewModel> {
        SearchViewModelFactory()
    }
    //adapter
    private lateinit var adapter: MostPopularAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViewModel()
    }

    private fun initView(){

    }

    private fun initViewModel(){
        viewModel.popularResults.observe(viewLifecycleOwner) { videos ->
            adapter.updateItems(videos.items)
        }
        viewModel.searchPopularVideo()
    }

    private fun initRecyclerView(){
        adapter = MostPopularAdapter(emptyList())
        binding.rvMostPopular.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvMostPopular.adapter = adapter

        // 아이템 간격 주기
        binding.rvMostPopular.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 40
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}