package com.example.subfundatiga.assets

import android.view.View

const val API_KEY = "c8c94119069a6fb9e98a4f0dac848d91eaf9a7ff"


fun View.setViewVisible(isTrue: Boolean) {
    if (isTrue) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}


fun View.setViewInvisible() {
    this.visibility = View.INVISIBLE
}