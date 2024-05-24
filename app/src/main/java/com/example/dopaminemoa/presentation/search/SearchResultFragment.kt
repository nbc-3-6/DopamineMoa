package com.example.dopaminemoa.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentSearchResultBinding
import com.example.dopaminemoa.mapper.model.VideoItemModel
import com.example.dopaminemoa.presentation.main.MainActivity
import com.example.dopaminemoa.presentation.searchshorts.SearchAdapter
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SearchViewModel by activityViewModels()

    private val adapter = SearchAdapter()
    private var isLoading = false
    private lateinit var searchText: String
    private lateinit var nextToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getText()
        backBtnPressed()

        observeData()
        makeRecyclerView()
        setClickListener()
    }

    /**
     * SearchActionFragment에서 담아준 text를 꺼내어 화면에 표시하는 함수입니다.
     */
    private fun getText() = with(binding) {
        val text = arguments?.getString(BUNDLE_KEY_FOR_RESULT_FRAGMENT) ?: ""
        searchText = text
        etSearch.setText(text)
        viewModel.searchVideoByText(text)
    }

    /**
     * 뒤로가기 버튼에 대한 처리를 하는 함수입니다.
     * 화면 상에 있는 백버튼 UI 또는 기기 자체의 백버튼 클릭 시 작동합니다.
     */
    private fun backBtnPressed() = with(binding) {
        ivBackBtn.setOnClickListener {
            parentFragment?.childFragmentManager?.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragment?.childFragmentManager?.popBackStack()
        }
    }

    /**
     * 통신으로 받아오는 데이터 상에 변화를 감지하는 함수입니다.
     * 변화 발생 시 recyclerView adapter에 데이터 변경에 대한 update를 요청합니다.
     * 통신에 에러 발생 시, 에러 타입을 구분하여 화면에 토스트를 출력합니다.
     */
    private fun observeData() = with(binding) {
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            rvSearch.visibility = View.VISIBLE
            tvNone.visibility = View.GONE

            isLoading = false

            adapter.updateList(items)
        }

        viewModel.searchResultErrorState.observe(viewLifecycleOwner) { errorState ->
            if (errorState) {
                val errorMessage = viewModel.errorMessage.value ?: getString(R.string.search_error)
                rvSearch.visibility = View.GONE
                tvNone.visibility = View.VISIBLE
                tvNone.text = errorMessage
            }
        }

        viewModel.nextPageToken.observe(viewLifecycleOwner) { token ->
            nextToken = token
        }
    }

    /**
     * recyclerView의 adapter와 click 리스너를 정의하는 함수입니다.
     * 스크롤 상태를 파악하여 데이터를 추가적으로 로딩합니다.
     */
    private fun makeRecyclerView() = with(binding) {
        rvSearch.adapter = adapter
        val layoutManager = GridLayoutManager(requireActivity(), 2)
        rvSearch.layoutManager = layoutManager

        adapter.itemClick = object : SearchAdapter.ItemClick {
            override fun onClick(view: View, item: VideoItemModel) {
                selectItem(item)
            }
        }
    }

    /**
     * 검색 버튼 및 키워드 버튼에 대한 리스너 처리를 모아놓은 함수입니다.
     */
    private fun setClickListener() = with(binding) {
        btnSearch.setOnClickListener {
            searchItem()
            setKeywordButtonColor(-1)
        }
        btnKeyword1.setOnClickListener {
            searchOnlyKeyword(it)
        }
        btnKeyword2.setOnClickListener {
            searchOnlyKeyword(it)
        }
        btnKeyword3.setOnClickListener {
            searchOnlyKeyword(it)
        }
        btnKeyword4.setOnClickListener {
            searchOnlyKeyword(it)
        }
    }

    /**
     * 검색 버튼을 클릭했을 때 실행되는 함수입니다.
     */
    private fun searchItem() = with(binding) {
        if (etSearch.text.toString().isNotEmpty()) {
            val text = etSearch.text.toString()
            reSearch(text)
        }
    }

    /**
     * 다시 검색을 요청하는 함수입니다.
     */
    private fun reSearch(text: String) {
        viewModel.searchVideoByText(text)
    }

    /**
     * 키워드 버튼을 클릭했을 때 실행되는 함수입니다.
     */
    private fun searchOnlyKeyword(view: View) = with(binding) {
        val searchText = etSearch.text.toString()
        when (view.id) {
            btnKeyword1.id -> {
                val text = searchText + " " + btnKeyword1.text.removePrefix("#").toString()
                reSearch(text)
                setKeywordButtonColor(btnKeyword1.id)
            }

            btnKeyword2.id -> {
                val text = searchText + " " + btnKeyword2.text.removePrefix("#").toString()
                reSearch(text)
                setKeywordButtonColor(btnKeyword2.id)
            }

            btnKeyword3.id -> {
                val text = searchText + " " + btnKeyword3.text.removePrefix("#").toString()
                reSearch(text)
                setKeywordButtonColor(btnKeyword3.id)
            }

            btnKeyword4.id -> {
                val text = searchText + " " + btnKeyword4.text.removePrefix("#").toString()
                reSearch(text)
                setKeywordButtonColor(btnKeyword4.id)
            }

            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    /**
     * 키워드 버튼에 대한 색상 처리를 수행하는 함수입니다.
     * 현재 검색에 적용된 버튼만 색상을 다르게 표시합니다.
     */
    private fun setKeywordButtonColor(id: Int) = with(binding) {
        when (id) {
            btnKeyword1.id -> {
                btnKeyword1.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnKeyword2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword4.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnKeyword2.id -> {
                btnKeyword1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword2.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnKeyword3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword4.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnKeyword3.id -> {
                btnKeyword1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword3.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
                btnKeyword4.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
            btnKeyword4.id -> {
                btnKeyword1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword4.setBackgroundResource(R.drawable.rec_keyword_btn_pressed)
            }
            else -> {
                btnKeyword1.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword2.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword3.setBackgroundResource(R.drawable.rec_keyword_btn)
                btnKeyword4.setBackgroundResource(R.drawable.rec_keyword_btn)
            }
        }
    }

    /**
     * recyclerView에서 item을 선택했을 때 실행되는 함수입니다.
     */
    private fun selectItem(item: VideoItemModel) {
        val bundle = Bundle().apply {
            putParcelable(BUNDLE_KEY_FOR_DETAIL_FRAGMENT, item)
        }

        val detailFragment = VideoDetailFragment.newInstance(bundle)
        (requireActivity() as MainActivity).showVideoDetailFragment(detailFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_KEY_FOR_DETAIL_FRAGMENT = "BUNDLE_KEY_FOR_DETAIL_FRAGMENT"
        const val BUNDLE_KEY_FOR_RESULT_FRAGMENT = "BUNDLE_KEY_FOR_RESULT_FRAGMENT"

        fun newInstanceForFragment(bundle: Bundle): SearchResultFragment {
            return SearchResultFragment().apply {
                arguments = bundle
            }
        }
    }
}