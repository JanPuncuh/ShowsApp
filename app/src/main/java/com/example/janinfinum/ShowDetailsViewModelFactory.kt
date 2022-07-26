package com.example.janinfinum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowDetailsViewModelFactory(val arg: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java).newInstance(arg)
    }
}