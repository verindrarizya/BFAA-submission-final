package com.example.subfundatiga.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.example.subfundatiga.R
import com.example.subfundatiga.adapters.SectionsPagerAdapter
import com.example.subfundatiga.databinding.ActivityUserBinding
import com.google.android.material.tabs.TabLayoutMediator

class UserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.detail, R.string.following, R.string.followers)
    }

    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.user_detail)
        }
        val username = intent.getStringExtra(EXTRA_USERNAME)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username!!)
        binding.vpUser.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.vpUser) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}