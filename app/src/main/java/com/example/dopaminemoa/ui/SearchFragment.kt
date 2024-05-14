package com.example.dopaminemoa.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dopaminemoa.R
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchItem()
        setCategoryClickListener()
    }

    private fun searchItem() = with(binding) {
        if (etSearch.text.toString().isNotEmpty()) {
            btnSearch.setOnClickListener {

                makeView()
            }
        }
    }

    private fun makeView() = with(binding) {
        rvSearch.adapter = adapter
        rvSearch.layoutManager = GridLayoutManager(requireActivity(), 2)

        adapter.itemClick = object : SearchAdapter.ItemClick {
            override fun onClick(view: View, item: VideoItemModel) {
                selectItem(item)
            }
        }
    }

    private fun setCategoryClickListener() = with(binding) {
        btnCategory1.setOnClickListener { SearchCategory(it) }
        btnCategory2.setOnClickListener { SearchCategory(it) }
        btnCategory3.setOnClickListener { SearchCategory(it) }
        btnCategory4.setOnClickListener { SearchCategory(it) }
    }

    private fun SearchCategory(view: View) = with(binding) {
        when(view.id) {
            R.id.btn_category1 -> {

            }
            R.id.btn_category2 -> {

            }
            R.id.btn_category3 -> {

            }
            R.id.btn_category4 -> {

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

    companion object {
        const val BUNDLE_KEY_FOR_DETAIL_FRAGMENT = "BUNDLE_KEY_FOR_DETAIL_FRAGMENT"
    }
}