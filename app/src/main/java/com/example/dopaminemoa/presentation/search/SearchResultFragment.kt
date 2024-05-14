package com.example.dopaminemoa.presentation.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentSearchBinding
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

        backBtnPressed()
        backToAction()
        getText()

        observeData()
        makeRecyclerView()
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

    private fun getText() = with(binding) {
        val text = arguments?.getString(BUNDLE_KEY_FOR_RESULT_FRAGMENT) ?: ""
        etSearch.setText(text)
    }

    private fun backBtnPressed() = with(binding) {
        ivBackBtn.setOnClickListener {
            parentFragment?.childFragmentManager?.popBackStack()
        }
    }

    private fun backToAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragment?.childFragmentManager?.popBackStack()
        }
    }

    private fun observeData() {
        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.updateList(it)
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