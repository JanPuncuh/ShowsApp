package com.example.janinfinum

import android.content.Context
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Response

class RegistrationViewModel : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun register(email: String, password: String, passwordRepeat: String, preferences: SharedPreferences) {
        val registerRequest = RegisterRequest(email, password, passwordRepeat)
        ApiModule.retrofit.register(registerRequest)
            .enqueue(object : retrofit2.Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        preferences.edit().putString("TOKEN", response.headers()["access-token"]).apply()
                        preferences.edit().putString("CLIENT", response.headers()["client"]).apply()
                        preferences.edit().putString("UID", response.headers()["uid"]).apply()
                        _success.value = true
                    }
                    else _success.value = false
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _success.value = false
                    return
                }
            })
    }
}