package com.example.subfundatiga.activities

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundatiga.R
import com.example.subfundatiga.adapters.UsersAdapter
import com.example.subfundatiga.assets.setViewVisible
import com.example.subfundatiga.data.database.ContractHelper
import com.example.subfundatiga.data.database.ContractHelper.CONTENT_URI
import com.example.subfundatiga.data.model.User
import com.example.subfundatiga.databinding.ActivityUserFavBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavBinding
    private lateinit var userFavList: ArrayList<User>
    private lateinit var adapter: UsersAdapter

    companion object {
        private const val EXTRA_USER_FAV_STATE = "extra_user_fav_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.user_favorite)
        }

        binding.rvUserFav.layoutManager = LinearLayoutManager(this)
        binding.rvUserFav.setHasFixedSize(true)

        adapter = UsersAdapter()
        binding.rvUserFav.adapter = adapter
        adapter.setOnItemClickCallback(object: UsersAdapter.OnItemCallback {
            override fun onItemClicked(data: User) {
                moveToUserDetail(data.username)
            }

        })

        val mHandlerThread = HandlerThread("UserFavObserver")
        mHandlerThread.start()
        val mHandler = Handler(mHandlerThread.looper)

        val userFavObserver = object: ContentObserver(mHandler) {
            override fun onChange(selfChange: Boolean) {
                loadUserFavAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, userFavObserver)

        if (savedInstanceState == null) {
            loadUserFavAsync()
        } else {
            userFavList = savedInstanceState.getParcelableArrayList<User>(EXTRA_USER_FAV_STATE) as ArrayList<User>
            adapter.setData(userFavList)
        }
    }

    private fun loadUserFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.setViewVisible(true)

            val defferedUserFav = async(Dispatchers.IO) {
                val mCursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                ContractHelper.mapCursorToArrayList(mCursor)
            }

            userFavList = defferedUserFav.await()
            binding.progressBar.setViewVisible(false)

            if (userFavList.size > 0) {
                adapter.setData(userFavList)
            } else {
                adapter.setData(ArrayList())
                Toast.makeText(this@UserFavActivity, "Empty Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USER_FAV_STATE, adapter.getData())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun moveToUserDetail(username: String?) {
        val intent = Intent(this, UserActivity::class.java).apply {
            putExtra(UserActivity.EXTRA_USERNAME, username)
        }
        startActivity(intent)
    }
}