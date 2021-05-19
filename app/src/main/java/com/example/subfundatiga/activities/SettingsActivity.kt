package com.example.subfundatiga.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.subfundatiga.R
import com.example.subfundatiga.databinding.ActivitySettingsBinding
import com.example.subfundatiga.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.settings)
        }

        supportFragmentManager.beginTransaction()
                .replace(binding.settingsHolder.id, SettingsFragment())
                .commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}