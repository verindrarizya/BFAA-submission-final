package com.example.subfundatiga.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subfundatiga.assets.API_KEY
import com.example.subfundatiga.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUsers(qUsername: String) {
        val listItems = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=$qUsername"

        val client  = AsyncHttpClient()
        client.addHeader("Authorization", "token $API_KEY")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val listUser = responseObject.getJSONArray("items")

                    for (i in 0 until listUser.length()) {
                        val userResp = listUser.getJSONObject(i)
                        val user = User()
                        user.imgProfile = userResp.getString("avatar_url")
                        user.username = userResp.getString("login")

                        listItems.add(user)
                    }
                    Log.d(TAG, listItems.toString())
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d(TAG, "Failure result exception: ${e.message.toString()}")
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
            ) {
                Log.d(TAG, "onFailure: ${error?.message.toString()}")
            }

        })

    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

}