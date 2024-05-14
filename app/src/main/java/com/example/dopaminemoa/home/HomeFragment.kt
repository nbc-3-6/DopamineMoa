package com.example.dopaminemoa.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dopaminemoa.databinding.FragmentHomeBinding
import com.example.dopaminemoa.viewmodel.VideoViewModel
import com.example.dopaminemoa.viewmodel.VideoViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: VideoViewModel by viewModels({ requireActivity() }) {
        VideoViewModelFactory.newInstance()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}