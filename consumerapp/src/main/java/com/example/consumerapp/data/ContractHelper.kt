package com.example.consumerapp.data

import android.database.Cursor
import android.net.Uri
import android.util.Log

object ContractHelper {

    const val AUTHORITY = "com.example.subfundatiga"
    const val SCHEME = "content"

    const val TABLE_NAME = "favorite_user_table"

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

    fun mapCursorToArrayList(userFavCursor: Cursor?): ArrayList<User> {
        val userFavList = ArrayList<User>()

        userFavCursor?.apply {
            while (moveToNext()) {
                val user = User()
                try {
                    user.imgProfile = getString(getColumnIndexOrThrow("img_profile"))
                    user.username = getString(getColumnIndexOrThrow("username"))
                    user.name = getString(getColumnIndexOrThrow("name"))
                    user.company = getString(getColumnIndexOrThrow("company"))
                    user.location = getString(getColumnIndexOrThrow("location"))
                    user.repoCount = getInt(getColumnIndexOrThrow("repo_count"))
                    user.followingCount = getInt(getColumnIndexOrThrow("following_count"))
                    user.followersCount = getInt(getColumnIndexOrThrow("followers_count"))

                    userFavList.add(user)
                } catch (e: Exception) {
                    Log.d("ContractHelper", e.message.toString())
                }
            }
        }

        return userFavList
    }

}