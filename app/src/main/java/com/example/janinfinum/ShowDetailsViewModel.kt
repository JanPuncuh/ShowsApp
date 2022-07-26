package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class ShowDetailsViewModel : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String> = _desc

    private val _img = MutableLiveData<Int>()
    val img: LiveData<Int> = _img


    private var reviewsList = ArrayList<Review>()


    private val _reviews = MutableLiveData<ArrayList<Review>>()
    val reviews: LiveData<ArrayList<Review>> = _reviews

    init {
        _reviews.value = reviewsList
    }

}