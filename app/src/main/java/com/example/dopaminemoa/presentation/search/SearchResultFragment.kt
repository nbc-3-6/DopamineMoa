package com.example.dopaminemoa.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dopaminemoa.data.remote.Resource
import com.example.dopaminemoa.databinding.FragmentSearchResultBinding
import com.example.dopaminemoa.mapper.VideoItemModel
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory


class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }

    private val adapter = SearchAdapter()

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

    private fun getText() = with(binding) {
        val text = arguments?.getString(BUNDLE_KEY_FOR_RESULT_FRAGMENT) ?: ""
        etSearch.setText(text)
    }

    private fun backBtnPressed() = with(binding) {
        ivBackBtn.setOnClickListener {
            parentFragment?.childFragmentManager?.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragment?.childFragmentManager?.popBackStack()
        }
    }

    private fun observeData() = with(binding) {
        viewModel.searchResults.observe(viewLifecycleOwner) { resource ->

            if (tvNone.visibility == View.VISIBLE) {
                tvNone.visibility = View.GONE
            }

            when (resource) {
                is Resource.Success -> {
                    adapter.updateList(resource.data ?: emptyList())
                }

                is Resource.Error -> {
                    val exception = resource.exception

                    when {
                        exception?.message?.contains("HTTP 400") == true -> {
                            Toast.makeText(requireActivity(), "잘못된 요청입니다.", Toast.LENGTH_SHORT).show()
                        }
                        exception?.message?.contains("HTTP 401") == true -> {
                            Toast.makeText(requireActivity(), "요청이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show()
                        }
                        exception?.message?.contains("HTTP 403") == true -> {
                            Toast.makeText(requireActivity(), "현재 기능을 일시적으로 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                        exception?.message?.contains("HTTP 404") == true -> {
//                            Toast.makeText(requireActivity(), "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show() //필요시 사용
                            tvNone.visibility = View.VISIBLE
                        }
                        else -> {
                            Toast.makeText(requireActivity(), "알 수 없는 문제가 생겼습니다. 고객센터에 문의하세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun makeRecyclerView() = with(binding) {
        rvSearch.adapter = adapter
        rvSearch.layoutManager = GridLayoutManager(requireActivity(), 2)

        adapter.itemClick = object : SearchAdapter.ItemClick {
            override fun onClick(view: View, item: VideoItemModel) {
                selectItem(item)
            }
        }
    }

    private fun setClickListener() = with(binding) {
        btnSearch.setOnClickListener {
            searchItem()
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

    private fun searchItem() = with(binding) {
        if (etSearch.text.toString().isNotEmpty()) {
            val text = etSearch.text.toString()
            reSearch(text)
        }
    }

    private fun reSearch(text: String) {
        viewModel.searchVideoByText(text)
    }

    private fun searchOnlyKeyword(view: View) = with(binding) {
        val searchText = etSearch.text.toString()
        when (view.id) {
            btnKeyword1.id -> {
                val text = searchText + btnKeyword1.text.removePrefix("#").toString()
                reSearch(text)
            }

            btnKeyword2.id -> {
                val text = searchText + btnKeyword2.text.removePrefix("#").toString()
                reSearch(text)
            }

            btnKeyword3.id -> {
                val text = searchText + btnKeyword3.text.removePrefix("#").toString()
                reSearch(text)
            }

            btnKeyword4.id -> {
                val text = searchText + btnKeyword4.text.removePrefix("#").toString()
                reSearch(text)
            }

            else -> throw IllegalArgumentException("Unknown button ID")
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