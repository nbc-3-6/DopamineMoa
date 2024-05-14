package com.example.dopaminemoa.presentation.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dopaminemoa.databinding.FragmentSearchBinding
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }

    private val adapter = SearchAdapter()

    private var isSearchingByText = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSearchingByText = false

        observeData()
        searchItem()
        setClickListener()
    }

    private fun observeData() {
        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private fun setClickListener() = with(binding) {
        btnSearch.setOnClickListener {
            isSearchingByText = true
            searchItem()
        }
        btnKeyword1.setOnClickListener {
            isSearchingByText = false
            SearchKeyword(it)
        }
        btnKeyword2.setOnClickListener {
            isSearchingByText = false
            SearchKeyword(it)
        }
        btnKeyword3.setOnClickListener {
            isSearchingByText = false
            SearchKeyword(it)
        }
        btnKeyword4.setOnClickListener {
            isSearchingByText = false
            SearchKeyword(it)
        }
    }

    private fun searchItem() = with(binding) {
        if (etSearch.text.toString().isNotEmpty()) {
            val text = etSearch.text.toString()
            requestSearchByKeyword(text)
            makeView()
        }
    }

    private fun requestSearchByKeyword(text: String) {
        viewModel.searchVideoByText(text)
        makeView()
    }

    private fun SearchKeyword(view: View) = with(binding) {
        if (isSearchingByText) { //검색 중일 때 키워드 추가 검색
            when(view.id) {
                btnKeyword1.id -> {

                }
                btnKeyword2.id -> {

                }
                btnKeyword3.id -> {

                }
                btnKeyword4.id -> {

                }
                else -> throw IllegalArgumentException("Unknown button ID")
            }
        } else { //검색어 없이 키워드만 검색
            when(view.id) {
                btnKeyword1.id -> {
                    requestSearchByKeyword(btnKeyword1.text.removePrefix("#").toString().toLowerCase())
                }
                btnKeyword2.id -> {
                    requestSearchByKeyword(btnKeyword2.text.removePrefix("#").toString().toLowerCase())
                }
                btnKeyword3.id -> {
                    requestSearchByKeyword(btnKeyword3.text.removePrefix("#").toString().toLowerCase())
                }
                btnKeyword4.id -> {
                    requestSearchByKeyword(btnKeyword4.text.removePrefix("#").toString().toLowerCase())
                }
                else -> throw IllegalArgumentException("Unknown button ID")
            }
        }
    }

    private fun makeView() = with(binding) {
        if (isSearchingByText) { //텍스트 검색 중
            hcvCategory.visibility = View.VISIBLE
        } else { //키워드 검색 중
            hcvCategory.visibility = View.GONE
        }

        rvSearch.visibility = View.VISIBLE
        tvNone.visibility = View.GONE

        rvSearch.adapter = adapter
        rvSearch.layoutManager = GridLayoutManager(requireActivity(), 2)

        adapter.itemClick = object : SearchAdapter.ItemClick {
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
            putParcelable(BUNDLE_KEY_FOR_DETAIL_FRAGMENT, item)
        }
    }

    companion object {
        const val BUNDLE_KEY_FOR_DETAIL_FRAGMENT = "BUNDLE_KEY_FOR_DETAIL_FRAGMENT"
        fun newInstance() = SearchFragment()
    }
}