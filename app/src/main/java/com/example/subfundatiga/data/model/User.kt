package com.example.subfundatiga.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.subfundatiga.data.database.ContractHelper.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class User(
        @ColumnInfo(name = "img_profile") var imgProfile: String? = null,
        @PrimaryKey @NonNull @ColumnInfo(name = "username") var username: String = "default_username",
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "company") var company: String? = null,
        @ColumnInfo(name = "location") var location: String? = null,
        @ColumnInfo(name = "repo_count") var repoCount: Int = 0,
        @ColumnInfo(name = "following_count") var followingCount: Int = 0,
        @ColumnInfo(name = "followers_count") var followersCount: Int = 0
): Parcelable
