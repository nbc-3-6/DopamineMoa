package com.example.dopaminemoa.presentation.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentSearchBinding
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFragment(SearchActionFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.cl_container, fragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    fun showSearchResultFragment(bundle: Bundle) {
        val searchResultFragment = SearchResultFragment.newInstanceForFragment(bundle)
        loadFragment(searchResultFragment)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}