package com.example.janinfinum

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ShowDetailsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _reviews = MutableLiveData<ArrayList<Review2>?>()
    val reviews: LiveData<ArrayList<Review2>?> = _reviews

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

    fun setReviews(reviews: ArrayList<Review2>?) {
        _reviews.value = reviews!!
    }

    fun getReviewsFromDatabase(id: String): LiveData<List<Review2>> {
        return database.reviewDao().getAllReviewsFromShow(id)
    }

    fun getShowFromDatabase(id: String): LiveData<Show2> {
        return database.showsDao().getShow(id)
    }

    fun fetchReview(showId: String, app: MyApplication) {
        ApiModule.retrofit.getReviews(showId, "Bearer", app.token!!, app.client!!, app.uid!!)
            .enqueue(object : retrofit2.Callback<ReviewResponse> {
                override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                    if (response.isSuccessful) {
                        val reviews = response.body()?.reviews
                        //saves all reviews from this show into DB
                        if (reviews != null) {
                            setReviews(response.body()?.reviews)
                        }
                    }
                    else {
                        _reviews.value = null
                        Log.d("TEST", "response not successful")
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    _reviews.value = null
                    Log.d("TEST", "${t.message.toString()}\n${t.printStackTrace()}")
                }
            })
    }

}