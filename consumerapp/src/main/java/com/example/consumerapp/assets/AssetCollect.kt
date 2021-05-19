package com.example.consumerapp.assets

import android.view.View

fun View.setViewVisible(isTrue: Boolean) {
    if (isTrue) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}