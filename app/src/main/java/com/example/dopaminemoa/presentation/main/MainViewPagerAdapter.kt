package com.example.dopaminemoa.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dopaminemoa.R
import com.example.dopaminemoa.presentation.home.HomeFragment
import com.example.dopaminemoa.presentation.myvideos.VideoDetailFragment
import com.example.dopaminemoa.presentation.search.SearchFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        MainTabModel(SearchFragment.newInstance(), R.string.main_tab_search_title, R.drawable.ic_search),
        MainTabModel(HomeFragment.newInstance(), R.string.main_tab_home_title, R.drawable.ic_home),
        MainTabModel(VideoDetailFragment.newInstance(), R.string.main_tab_myvideos_title, R.drawable.ic_my_videos),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].fragment

    fun getTitle(position: Int): Int = fragments[position].title

    fun getTabIcon(position: Int): Int = fragments[position].icon
}