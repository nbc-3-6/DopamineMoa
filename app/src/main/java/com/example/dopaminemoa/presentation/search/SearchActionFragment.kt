package com.example.dopaminemoa.presentation.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dopaminemoa.databinding.FragmentSearchActionBinding
import com.example.dopaminemoa.presentation.search.SearchResultFragment.Companion.BUNDLE_KEY_FOR_RESULT_FRAGMENT
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory

class SearchActionFragment : Fragment() {

    private var _binding: FragmentSearchActionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchActionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListener()
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
            setFragmentMoving(text)
        }
    }

    private fun searchOnlyKeyword(view: View) = with(binding) {
        when (view.id) {
            btnKeyword1.id -> {
                val text = btnKeyword1.text.removePrefix("#").toString()
                setFragmentMoving(text)
            }
            btnKeyword2.id -> {
                val text = btnKeyword2.text.removePrefix("#").toString()
                setFragmentMoving(text)
            }
            btnKeyword3.id -> {
                val text = btnKeyword3.text.removePrefix("#").toString()
                setFragmentMoving(text)
            }
            btnKeyword4.id -> {
                val text = btnKeyword4.text.removePrefix("#").toString()
                setFragmentMoving(text)
            }
            else -> throw IllegalArgumentException("Unknown button ID")
        }
    }

    private fun setFragmentMoving(text: String) {
//        viewModel.searchVideoByText(text)

        val bundle = Bundle().apply {
            putString(BUNDLE_KEY_FOR_RESULT_FRAGMENT, text)
        }

        val fragmentResult = SearchResultFragment()
        fragmentResult.arguments = bundle

        (parentFragment as? SearchFragment)?.showSearchResultFragment(bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}