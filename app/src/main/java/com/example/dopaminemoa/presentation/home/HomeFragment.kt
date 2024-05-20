package com.example.dopaminemoa.presentation.home

import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
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
import com.example.dopaminemoa.presentation.home.adapter.ChannelsAdapter
import com.example.dopaminemoa.presentation.home.adapter.VideosAdapter
import com.example.dopaminemoa.presentation.home.adapter.VideoCategoriesAdapter
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }
    //adapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var categoryAdapter: VideoCategoriesAdapter
    private lateinit var channelsAdapter: ChannelsAdapter
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
        initViewModel()
        initErrorViewModel()
        initMostPopularRecyclerView()
        initVideoCategoryRecyclerView()
        initChannelsRecyclerView()
        initSpinner()
    }


    private fun initViewModel() {
        viewModel.popularResults.observe(viewLifecycleOwner) { videos ->
            videosAdapter.submitList(videos)
        }
        viewModel.categoryVideoResults.observe(viewLifecycleOwner) { categories ->
            val categoryList = categories.map { it.title }
            categorySpinnerAdapter.clear()
            if (categoryList.isNotEmpty()) {
                categorySpinnerAdapter.addAll(categoryList)
            }
        }
        viewModel.videoListByCategory.observe(viewLifecycleOwner) { videos ->
            categoryAdapter.submitList(videos)
        }
        viewModel.channelIds.observe(viewLifecycleOwner) { channelId ->
            viewModel.searchChannelByCategory(channelId)
            Log.d("채널 아이디","$channelId")
        }
        viewModel.categoryChannelResults.observe(viewLifecycleOwner){ channelsItem ->
            channelsAdapter.submitList(channelsItem)
        }
        viewModel.selectedCategory.observe(viewLifecycleOwner) { selectedCategory ->
            binding.tvChannelTitle.text = selectedCategory
        }

        viewModel.searchPopularVideo()
        viewModel.takeVideoCategories()
    }

    private fun initErrorViewModel(){
        //dialog
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showErrorDialog(it)
            }
        }
        //MostPopular
        viewModel.mostPopularErrorState.observe(viewLifecycleOwner) {
            toggleRecyclerViewAndErrorView(binding.rvMostPopular, binding.ivMostPopularError, it)
        }

        //Category
        viewModel.categoryErrorState.observe(viewLifecycleOwner) {
            toggleRecyclerViewAndErrorView(binding.rvCategory, binding.ivCategoryError, it)
            toggleRecyclerViewAndErrorView(binding.rvChannels, binding.ivChannelError, it)
        }
    }
    private fun toggleRecyclerViewAndErrorView(recyclerView: RecyclerView, errorView: View, state: Boolean) {
        recyclerView.visibility = if (state) View.GONE else View.VISIBLE

        errorView.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun initMostPopularRecyclerView() {
        videosAdapter = VideosAdapter(emptyList())
        binding.rvMostPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMostPopular.adapter = videosAdapter

        setGapRecyclerViewItem(binding.rvMostPopular)
    }

    private fun initVideoCategoryRecyclerView() {
        categoryAdapter = VideoCategoriesAdapter(emptyList())
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryAdapter
        setGapRecyclerViewItem(binding.rvCategory)
    }

    private fun initChannelsRecyclerView(){
        channelsAdapter = ChannelsAdapter(emptyList())
        binding.rvChannels.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvChannels.adapter = channelsAdapter
        setGapRecyclerViewItem(binding.rvChannels)
    }

    private fun initSpinner() {
        categorySpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.snCategory.adapter = categorySpinnerAdapter
        //카테고리 선택
        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //선택한 카테고리 받기
                val categoryId = viewModel.categoryVideoResults.value?.getOrNull(position)?.id
                categoryId?.let { viewModel.searchVideoByCategory(it) }
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                viewModel.updateSelectedCategory(selectedCategory)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //No action needed
            }
        }
    }

    //아이템 간격
    private fun setGapRecyclerViewItem(recyclerView: RecyclerView, rightSpace: Int= 40) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = rightSpace
            }
        })
    }
    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("API Error 발생")
            .setMessage(message)
            .setPositiveButton("앱 종료") { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}