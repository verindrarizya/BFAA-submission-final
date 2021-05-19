package com.example.consumerapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var imgProfile: String? = null,
        var username: String = "default_username",
        var name: String? = null,
        var company: String? = null,
        var location: String? = null,
        var repoCount: Int = 0,
        var followingCount: Int = 0,
        var followersCount: Int = 0
): Parcelable
