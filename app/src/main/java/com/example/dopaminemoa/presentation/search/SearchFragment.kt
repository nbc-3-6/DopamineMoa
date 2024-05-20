package com.example.dopaminemoa.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.FragmentSearchBinding

/**
 * 아래 2개의 하위 Fragment 간의 화면 전환을 관리하는 Fragment입니다.
 * SearchActionFragment : 검색창 화면
 * SearchResultFragment : 검색 결과 화면
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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

    /**
     * 하위 fragment에 대한 transaction을 처리하는 함수입니다.
     */
    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.cl_container, fragment)
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .commit()
    }

    /**
     * 검색 버튼 클릭 시 실행되어야 하는 fragment 전환 처리에 대한 함수입니다.
     * 검색어를 bundle로 저장하여 함께 전달합니다.
     */
    fun showSearchResultFragment(bundle: Bundle) {
        val searchResultFragment = SearchResultFragment.newInstanceForFragment(bundle)
        loadFragment(searchResultFragment)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}