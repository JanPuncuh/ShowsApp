package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowDetailsViewModel : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _desc = MutableLiveData<String>()
    val desc: LiveData<String> = _desc

    private val _img = MutableLiveData<String>()
    val img: LiveData<String> = _img

    fun setShowDetails(title: String, desc: String, img: String) {
        _title.value = title
        _desc.value = desc
        _img.value = img
    }

    private val _reviews = MutableLiveData<ArrayList<Review2>>()
    var reviews: LiveData<ArrayList<Review2>> = _reviews

    //init {}

    private val _avg = MutableLiveData<Float>()
    val avg: LiveData<Float> = _avg

    //sets the average reviews score
    fun averageRating(list: ArrayList<Review2>) {
        var rating = 0F
        list.forEach { review ->
            rating += review.rating
        }
        rating /= list.count()
        _avg.value = rating
        return
    }

    //sets list of reviews
    fun onResponseAPI(reviews: ArrayList<Review2>?) {
        _reviews.value = reviews!!
        this.reviews = _reviews
    }

    fun add(review: Review2) {
        _reviews.value?.add(review)
        reviews = _reviews
    }

}