package com.example.dopaminemoa.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.ActivityMainBinding
import com.example.dopaminemoa.presentation.videodetail.VideoDetailFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        // TabLayout x ViewPager2
        vpMain.adapter = viewPagerAdapter
        vpMain.offscreenPageLimit = viewPagerAdapter.itemCount
        vpMain.isUserInputEnabled = false

        TabLayoutMediator(tlMain, vpMain) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(viewPagerAdapter.getTabIcon(position))
        }.attach()

        tlMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.icon?.setTint(getColor(R.color.white))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.icon?.setTint(getColor(R.color.darkRed))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 재선택 시
            }
        })
        tlMain.getTabAt(1)?.select() // 홈을 default로 설정
    }

    private fun showVideoDetailFragment() {
        showToolbar(false)
        showTabLayout(false)

        val fragment = VideoDetailFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fl_video_detail, fragment)
            addToBackStack(null)
        }
    }

    private fun showToolbar(show: Boolean) {
        binding.tbMain.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showTabLayout(show: Boolean) {
        binding.tlMain.visibility = if (show) View.VISIBLE else View.GONE
    }

}