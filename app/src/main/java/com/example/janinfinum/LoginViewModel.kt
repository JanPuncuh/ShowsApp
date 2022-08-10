package com.example.janinfinum

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _successfulLogin = MutableLiveData<Boolean>()
    val successfulLogin: LiveData<Boolean> = _successfulLogin

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _client = MutableLiveData<String>()
    val client: LiveData<String> = _client

    private val _uid = MutableLiveData<String>()
    val uid: LiveData<String> = _uid

    fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        ApiModule.retrofit.login(loginRequest)
            .enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        _successfulLogin.value = true

                        _token.value = response.headers()["access-token"]
                        _client.value = response.headers()["client"]
                        _uid.value = email

                    }
                    else if (!response.isSuccessful) {
                        _successfulLogin.value = false
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("TEST", "${t.message.toString()}\n${t.stackTraceToString()}")
                    _successfulLogin.value = false
                }

            })
    }

}
