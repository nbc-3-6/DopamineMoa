package com.example.dopaminemoa

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dopaminemoa.databinding.FragmentMyVideoBinding
import com.example.dopaminemoa.presentation.home.HomeFragment
import com.example.dopaminemoa.presentation.search.SearchFragment

class MyVideoFragment : Fragment() {

    private lateinit var binding: FragmentMyVideoBinding
    private lateinit var adapter: MyVideoAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager
    private lateinit var mContext: Context

    companion object {
        fun newInstance() = MyVideoFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyVideoBinding.inflate(inflater, container, false)
        return binding.root
        setupViews()
    }

    private fun setupViews() {
        // RecyclerView 설정
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvMyVideo.layoutManager = gridmanager

        adapter = MyVideoAdapter(mContext)
        binding.rvMyVideo.adapter = adapter
        binding.rvMyVideo.itemAnimator = null
    }
}