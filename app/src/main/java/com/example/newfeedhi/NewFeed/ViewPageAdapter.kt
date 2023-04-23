package com.example.newfeedhi

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.newfeedhi.NewFeed.FriendRequestFragment
import com.example.newfeedhi.NewFeed.HomeFragment
import com.example.newfeedhi.NewFeed.MenuFragment
import com.example.newfeedhi.NewFeed.NotiFragment

class ViewPageAdapter(fragmentManager: FragmentManager, behavior: Int): FragmentStatePagerAdapter(fragmentManager) {


    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return HomeFragment()
            }
            1 -> {
                return FriendRequestFragment()
            }
            2 -> {
                return NotiFragment()
            }
            3 -> {
                return MenuFragment()
            }
            else -> {
                return HomeFragment()
            }
        }
    }

}