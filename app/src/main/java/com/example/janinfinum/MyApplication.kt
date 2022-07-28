package com.example.janinfinum

import android.app.Application

class MyApplication : Application() {

    var token: String? = null
    var uid: String? = null
    var client: String? = null

    override fun onCreate() {
        super.onCreate()
    }
}