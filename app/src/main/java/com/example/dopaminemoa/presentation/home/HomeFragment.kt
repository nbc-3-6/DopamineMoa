package com.example.dopaminemoa.presentation.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemoa.databinding.FragmentHomeBinding
import com.example.dopaminemoa.presentation.home.video.adapter.MostPopularAdapter
import com.example.dopaminemoa.presentation.home.videocategory.adapter.VideoCategoryAdapter
import com.example.dopaminemoa.viewmodel.SearchViewModelFactory
import com.example.dopaminemoa.viewmodel.VideoViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<VideoViewModel> {
        SearchViewModelFactory()
    }

    //adapter
    private lateinit var mostPopularAdapter: MostPopularAdapter
    private lateinit var categoryAdapter: VideoCategoryAdapter
    private lateinit var categorySpinnerAdapter: ArrayAdapter<String>



    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMostPopularRecyclerView()
        initViewModel()
        initSpinner()
        initVideoCategoryRecyclerView()
    }


    private fun initViewModel() {
        //mostPopular
        viewModel.popularResults.observe(viewLifecycleOwner) { videos ->
            mostPopularAdapter.updateItems(videos.items)
        }
        viewModel.categoryVideoResults.observe(viewLifecycleOwner) { categoryEntity ->
            val categoryList = categoryEntity.items.map { it.snippet.title }
            categorySpinnerAdapter.clear()
            if (categoryList.isNotEmpty()) {
                categorySpinnerAdapter.addAll(categoryList)
            }
        }

        viewModel.searchPopularVideo()
        viewModel.takeVideoCategories()
    }

    private fun initMostPopularRecyclerView() {
        mostPopularAdapter = MostPopularAdapter(emptyList())
        binding.rvMostPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostPopular.adapter = mostPopularAdapter

        // 아이템 간격 주기
        binding.rvMostPopular.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 40
            }
        })
    }

    private fun initVideoCategoryRecyclerView(){
        categoryAdapter = VideoCategoryAdapter(emptyList())
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryAdapter

        // 아이템 간격 주기
        binding.rvMostPopular.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 40
            }
        })
    }

    private fun initSpinner() {
        categorySpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.snCategory.adapter = categorySpinnerAdapter

        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //선택한 카테고리 받기
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}