package com.example.dopaminemoa.presentation.searchshorts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentShortsBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.network.RepositoryClient
import com.example.dopaminemoa.presentation.main.MainActivity
import com.example.dopaminemoa.presentation.shorts.SearchShortsAdapter
import com.example.dopaminemoa.presentation.shorts.ShortsViewModel
import com.example.dopaminemoa.presentation.shorts.ShortsViewModelFactory
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment


class SearchShortsFragment : Fragment() {
    private var _binding: FragmentShortsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShortsViewModel by viewModels {
        ShortsViewModelFactory(RepositoryClient.youtubeService, requireContext())
    }
    private val adapter = SearchShortsAdapter()
    private var isLoading = false
    private lateinit var searchText: String
    private lateinit var nextToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShortsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        setClickListener()
        observeData()
        makeRecyclerView()
    }

    /**
     * 최초 진입 시 보여줄 데이터를 요청하는 함수입니다.
     */
    private fun setInitialData() = with(binding) {
        searchOnlyCategory(btnCategory1)
    }

    /**
     * 각 카테고리 버튼에 대한 리스너 처리를 모아놓은 함수입니다.
     */
    private fun setClickListener() = with(binding) {
        btnCategory1.setOnClickListener {
            searchOnlyCategory(it)
        }
        btnCategory2.setOnClickListener {
            searchOnlyCategory(it)
        }
        btnCategory3.setOnClickListener {
            searchOnlyCategory(it)
        }
        btnCategory4.setOnClickListener {
            searchOnlyCategory(it)
        }
        btnCategory5.setOnClickListener {
            searchOnlyCategory(it)
        }
    }

    /**
     * 각 카테고리 버튼을 클릭했을 때 실행되는 함수입니다.
     */
    private fun searchOnlyCategory(view: View) = with(binding) {
        when (view.id) {
            btnCategory1.id -> {
                val text = btnCategory1.text.toString() + " #shorts"
                searchText = text

                viewModel.searchVideoByTextForShorts(text)
                setCategoryButtonColor(btnCategory1.id)
            }
            btnCategory2.id -> {
                val text = btnCategory2.text.toString() + " #shorts"
                searchText = text

                viewModel.searchVideoByTextForShorts(text)
                setCategoryButtonColor(btnCategory2.id)
            }
            btnCategory3.id -> {
                val text = btnCategory3.text.toString() + " #shorts"
                searchText = text

                viewModel.searchVideoByTextForShorts(text)
                setCategoryButtonColor(btnCategory3.id)
            }
            btnCategory4.id -> {
                val text = btnCategory4.text.toString() + " #shorts"
                searchText = text

                viewModel.searchVideoByTextForShorts(text)
                setCategoryButtonColor(btnCategory4.id)
            }
            btnCategory5.id -> {
                val text = btnCategory5.text.toString() + " #shorts"
                searchText = text

                viewModel.searchVideoByTextForShorts(text)
                setCategoryButtonColor(btnCategory5.id)
            }
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    /**
     * 카테고리 버튼에 대한 색상 처리를 수행하는 함수입니다.
     * 현재 검색에 적용된 버튼만 색상을 다르게 표시합니다.
     */
    private fun setCategoryButtonColor(id: Int) = with(binding) {
        when (id) {
            btnCategory1.id -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnCategory2.id -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnCategory3.id -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnCategory4.id -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnCategory5.id -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
            }
            else -> {
                btnCategory1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory4.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnCategory5.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
        }
    }

    /**
     * 통신으로 받아오는 데이터 상에 변화를 감지하는 함수입니다.
     * 변화 발생 시 recyclerView adapter에 데이터 변경에 대한 update를 요청합니다.
     * 통신에 에러 발생 시, 에러 타입을 구분하여 화면에 토스트를 출력합니다.
     */
    private fun observeData() {
        viewModel.searchResultsForShorts.observe(viewLifecycleOwner) { items ->
            isLoading = false
            adapter.deleteLoading()

            items?.let { adapter.addItems(it) }
        }

        viewModel.searchResultErrorState.observe(viewLifecycleOwner) { errorState ->
            if (errorState) {
                val errorMessage = viewModel.errorMessage.value ?: "알 수 없는 문제가 생겼습니다."
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.nextPageToken.observe(viewLifecycleOwner) { token ->
            nextToken = token
        }
    }

    /**
     * recyclerView 그리는 역할을 하는 함수입니다.
     * 스크롤 상태를 파악하여 데이터를 추가적으로 로딩합니다.
     */
    private fun makeRecyclerView() = with(binding) {
        rvShorts.adapter = adapter
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        adapter.setupSpanSizeLookup(layoutManager)
        rvShorts.layoutManager = layoutManager

        rvShorts.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (isLoading.not() && lastVisibleItemPosition == totalItemCount - 1) {
                    isLoading = true
                    adapter.showLoadingView()
                    viewModel.searchMoreVideoByTextForShorts(searchText, nextToken)
                }
            }
        })

        adapter.itemClick = object : SearchShortsAdapter.ItemClick {
            override fun onClick(view: View, item: VideoItemModel) {
                selectItem(item)
            }
        }
    }

    /**
     * recyclerView에서 item을 선택했을 때 실행되는 함수입니다.
     * Detail fagment로 이동시 데이터를 bundle로 넘기고 fragment를 전환하거나 하는 식의 코드가 필요합니다.
     */
    private fun selectItem(item: VideoItemModel) {
        val bundle = Bundle().apply {
            putParcelable(BUNDLE_KEY_FOR_DETAIL_FRAGMENT_FROM_SHORTS, item)
        }

        val detailFragment = VideoDetailFragment.newInstance(bundle)
        (requireActivity() as MainActivity).showVideoDetailFragment(detailFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_KEY_FOR_DETAIL_FRAGMENT_FROM_SHORTS = "BUNDLE_KEY_FOR_DETAIL_FRAGMENT_FROM_SHORTS"

        fun newInstance() = SearchShortsFragment()
    }
}