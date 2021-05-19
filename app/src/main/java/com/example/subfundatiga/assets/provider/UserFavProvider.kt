package com.example.subfundatiga.assets.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.subfundatiga.data.database.AppDatabase
import com.example.subfundatiga.data.database.ContractHelper
import com.example.subfundatiga.data.database.UserFavDao

class UserFavProvider: ContentProvider() {

    companion object {
        private const val USER_FAV = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(ContractHelper.AUTHORITY, ContractHelper.TABLE_NAME, USER_FAV)
        }
    }

    private lateinit var userFavDao: UserFavDao

    override fun onCreate(): Boolean {
        userFavDao = context?.let { AppDatabase.getDatabase(it).userFavDao() }!!
        return false
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER_FAV -> userFavDao.getFavoriteUser()
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}