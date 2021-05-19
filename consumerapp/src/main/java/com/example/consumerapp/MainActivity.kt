package com.example.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.UsersAdapter
import com.example.consumerapp.assets.setViewVisible
import com.example.consumerapp.data.ContractHelper
import com.example.consumerapp.data.ContractHelper.CONTENT_URI
import com.example.consumerapp.data.User
import com.example.consumerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userFavList: ArrayList<User>
    private lateinit var adapter: UsersAdapter

    companion object {
        private const val EXTRA_USE_FAV_STATE = "extra_user_fav_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUserFav.layoutManager = LinearLayoutManager(this)
        binding.rvUserFav.setHasFixedSize(true)

        adapter = UsersAdapter()
        binding.rvUserFav.adapter = adapter

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
            userFavList = savedInstanceState.getParcelableArrayList<User>(EXTRA_USE_FAV_STATE) as ArrayList<User>
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
                Toast.makeText(this@MainActivity, "Empty Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_USE_FAV_STATE, adapter.getData())
    }
}