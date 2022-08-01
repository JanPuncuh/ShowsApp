package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowDetailsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _reviews = MutableLiveData<ArrayList<Review2>>()
    val reviews: LiveData<ArrayList<Review2>> = _reviews

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
    }

    fun getReviewsFromDatabase(id: String): LiveData<List<Review2>> {
        return database.reviewDao().getAllReviewsFromShow(id)
    }

    fun getShowFromDatabase(id: String): LiveData<Show2> {
        return database.showsDao().getShow(id)
    }

}