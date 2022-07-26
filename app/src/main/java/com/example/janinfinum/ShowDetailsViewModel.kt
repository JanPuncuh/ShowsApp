package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowDetailsViewModel : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String> = _desc

    private val _img = MutableLiveData<Int>()
    val img: LiveData<Int> = _img

    fun setShowDetails(title: String, desc: String, img: Int) {
        _title.value = title
        _desc.value = desc
        _img.value = img
    }

    private var reviewsList = ArrayList<Review>()

    private val _reviews = MutableLiveData<ArrayList<Review>>()
    val reviews: LiveData<ArrayList<Review>> = _reviews

    init {
        _reviews.value = reviewsList
    }

    private val _avg = MutableLiveData<Float>()
    val avg: LiveData<Float> = _avg

    //sets the avg
    fun averageRating(list: ArrayList<Review>) {
        var rating = 0F
        list.forEach { review ->
            rating += review.rating
        }
        rating /= list.count()
        _avg.value = rating
        return
    }

}