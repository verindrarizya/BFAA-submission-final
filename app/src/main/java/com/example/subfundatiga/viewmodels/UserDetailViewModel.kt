package com.example.subfundatiga.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.subfundatiga.assets.API_KEY
import com.example.subfundatiga.data.database.AppDatabase
import com.example.subfundatiga.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class UserDetailViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private val TAG = UserDetailViewModel::class.java.simpleName
    }

    // get DAO of User Instance
    private var userFavDao = AppDatabase.getDatabase(application.applicationContext).userFavDao()

    // User Data Fetch by API
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>
        get() = _user

    // state for user is already in db or not
    private val _currentUserExists: MutableLiveData<Boolean> = MutableLiveData()
    val currentUserExists: LiveData<Boolean>
        get() = _currentUserExists

    fun setUser(qUsername: String) {
        val url = "https://api.github.com/users/$qUsername"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $API_KEY")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val respObject = JSONObject(result)

                try {
                    val userDetail = User()

                    userDetail.username = respObject.getString("login")
                    userDetail.imgProfile = respObject.getString("avatar_url")

                    userDetail.name = checkNull(respObject.getString("name"))

                    userDetail.company = checkNull(respObject.getString("company"))

                    userDetail.location = checkNull(respObject.getString("location"))

                    userDetail.repoCount = respObject.getInt("public_repos")
                    userDetail.followingCount = respObject.getInt("following")
                    userDetail.followersCount = respObject.getInt("followers")

                    Log.d(TAG, "onSuccess: $userDetail")

                    _user.postValue(userDetail)
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: ${e.message.toString()}")
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


    private fun checkNull(q: String): String {
        return if (q == "null" || q.isNullOrEmpty()) "Not Available" else q
    }

    // Local Database Proses from here onwards

    fun insertUser(currentUser: User) {
        viewModelScope.launch {
            userFavDao.insert(currentUser)
            _currentUserExists.postValue(true)
        }
    }

    fun deleteUser(currentUser: User) {
        viewModelScope.launch {
            userFavDao.delete(currentUser)
            _currentUserExists.postValue(false)
        }
    }

    fun checkUserAlreadyFavInDb(qUsername: String) {
        viewModelScope.launch {
            val state = userFavDao.isUserAlreadyFav(qUsername)
            _currentUserExists.postValue(state)
        }
    }

}