package com.example.subfundatiga.data.database

import android.database.Cursor
import androidx.room.*
import com.example.subfundatiga.data.model.User

@Dao
interface UserFavDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT EXISTS(SELECT * FROM favorite_user_table WHERE username=:qUsername)")
    suspend fun isUserAlreadyFav(qUsername: String): Boolean

    @Query("SELECT * FROM favorite_user_table")
    fun getFavoriteUser(): Cursor

}