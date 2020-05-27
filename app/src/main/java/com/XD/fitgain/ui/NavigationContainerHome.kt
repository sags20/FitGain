package com.XD.fitgain.ui

import PagerViewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import com.XD.fitgain.R
import com.XD.fitgain.databinding.ActivityNavigationContainerHomeBinding

class NavigationContainerHome : AppCompatActivity() {

    private lateinit var binding : ActivityNavigationContainerHomeBinding
    private lateinit var mPagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationContainerHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mPagerAdapter = PagerViewAdapter(supportFragmentManager)
        binding.viewPager.adapter = mPagerAdapter
        binding.viewPager.offscreenPageLimit = 4

        binding.viewPager.currentItem = 0

        binding.chipNavigationBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.bussines -> binding.viewPager.currentItem = 1
                R.id.Activity -> binding.viewPager.currentItem = 2
                R.id.Account -> binding.viewPager.currentItem = 3
            }
        }
    }
}
