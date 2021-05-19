package com.example.subfundatiga.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundatiga.R
import com.example.subfundatiga.adapters.UsersAdapter
import com.example.subfundatiga.assets.setViewVisible
import com.example.subfundatiga.databinding.ActivityMainBinding
import com.example.subfundatiga.data.model.User
import com.example.subfundatiga.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemCallback {
            override fun onItemClicked(data: User) {
                moveToUserDetail(data.username)
            }
        })

        mainViewModel.getUsers().observe(this, {
            if(it.isNotEmpty()) {
                binding.progressBar.setViewVisible(false)
                binding.tvWelcomeStatement.setViewVisible(false)
                adapter.setData(it)
            } else {
                adapter.setData(ArrayList())
                binding.progressBar.setViewVisible(false)
                binding.tvWelcomeStatement.setViewVisible(true)
                binding.tvWelcomeStatement.text = getString(R.string.search_result_is_empty)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                binding.tvWelcomeStatement.setViewVisible(false)
                binding.progressBar.setViewVisible(true)
                mainViewModel.setUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = true
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            R.id.favorite_user -> startActivity(Intent(this@MainActivity, UserFavActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveToUserDetail(username: String?) {
        val intent = Intent(this, UserActivity::class.java).apply {
            putExtra(UserActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
    }
}