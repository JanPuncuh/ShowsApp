package com.example.janinfinum

import android.app.Application

class MyApplication : Application() {

    //logged user data for API headers
    var token: String? = null
    var uid: String? = null
    var client: String? = null
    var user: User? = null

}