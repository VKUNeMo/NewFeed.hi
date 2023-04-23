package com.example.newfeedhi.NewFeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newfeedhi.R
import com.example.newfeedhi.ViewPageAdapter
import com.example.newfeedhi.databinding.ActivityStartedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class StartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartedBinding
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }
    private fun initUI() {
        viewPager = binding.viewPager
        bottomNavigationView = binding.bottomNav

        val viewPagerAdapter = ViewPageAdapter(
            fragmentManager = supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = viewPagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tag_home -> {
                    viewPager.currentItem = 0
                }
                R.id.tag_friend -> {
                    viewPager.currentItem = 1
                }
                R.id.tag_noti -> {
                    viewPager.currentItem = 2
                }
                R.id.tag_menu -> {
                    viewPager.currentItem = 3
                }

            }
            true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_home).isChecked = true

                    }
                    1 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_friend).isChecked = true

                    }
                    2 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_noti).isChecked = true

                    }
                    3 -> {
                        bottomNavigationView.menu.findItem(R.id.tag_menu).isChecked = true

                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        }
        )
        }
}
