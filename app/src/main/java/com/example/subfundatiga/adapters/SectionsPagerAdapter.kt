package com.example.subfundatiga.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.subfundatiga.fragments.UserDetailFragment
import com.example.subfundatiga.fragments.UserFollowersFragment
import com.example.subfundatiga.fragments.UserFollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserDetailFragment().newFragmentInstance(username)
            1 -> fragment = UserFollowingFragment().newFragmentInstance(username)
            2 -> fragment = UserFollowersFragment().newFragmentInstance(username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 3
}