package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {

    private val _registrationResultLiveData = MutableLiveData<Boolean>()
    val registrationResultLiveData = _registrationResultLiveData

}