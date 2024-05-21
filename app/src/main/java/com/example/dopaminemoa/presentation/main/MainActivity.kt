package com.example.dopaminemoa.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.dopaminemoa.R
import com.example.dopaminemoa.databinding.ActivityMainBinding
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
        vpMain.currentItem = 1 // 초기 페이지를 홈 프래그먼트로 설정

        vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                showToolbar(position == 1) // 홈 프래그먼트일 때만 툴바 보이기
            }
        })

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

    fun showVideoDetailFragment(detailFragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.zoom_in,
                R.anim.zoom_out,
                R.anim.zoom_in,
                R.anim.zoom_out
            )
            replace(R.id.fl_video_detail, detailFragment)
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