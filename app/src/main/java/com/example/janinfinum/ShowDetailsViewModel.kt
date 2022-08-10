package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ShowDetailsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _reviews = MutableLiveData<ArrayList<Review>?>()
    val reviews: LiveData<ArrayList<Review>?> = _reviews

    private val _show = MutableLiveData<Show?>()
    val show: LiveData<Show?> = _show

    private val _postedReview = MutableLiveData<Review?>()
    val postedReview: LiveData<Review?> = _postedReview

    private val _avg = MutableLiveData<Float>()
    val avg: LiveData<Float> = _avg

    //sets the average reviews score
    fun averageRating(list: ArrayList<Review>) {
        var rating = 0F
        list.forEach { review ->
            rating += review.rating
        }
        rating /= list.count()
        _avg.value = rating
        return
    }

    fun setReviews(reviews: ArrayList<Review>?) {
        _reviews.value = reviews!!
    }

    fun getReviewsFromDatabase(id: String): LiveData<List<Review>> {
        return database.reviewDao().getAllReviewsFromShow(id)
    }

    fun getShowFromDatabase(id: String): LiveData<Show> {
        return database.showsDao().getShow(id)
    }

    //API call for getting shows details
    fun getShow(showId: String, app: MyApplication) {
        ApiModule.retrofit.getShow(showId, "Bearer", app.token!!, app.client!!, app.uid!!)
            .enqueue(object : retrofit2.Callback<ShowDetailsResponse> {
                override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                    if (response.isSuccessful) {
                        _show.value = response.body()?.show!!
                    }
                    _show.value = null
                }

                override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                    _show.value = null
                }
            })
    }

    //API call for getting reviews
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
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    _reviews.value = null
                }
            })
    }

    //API call for posting new review
    fun postReview(reviewRequest: ReviewRequest, app: MyApplication) {
        ApiModule.retrofit.postReview("Bearer", app.token!!, app.client!!, app.uid!!, reviewRequest)
            .enqueue(object : retrofit2.Callback<AddedReviewResponse> {
                override fun onResponse(call: Call<AddedReviewResponse>, response: Response<AddedReviewResponse>) {
                    if (response.isSuccessful) {
                        _postedReview.value = response.body()?.review
                    }
                    else _postedReview.value = null
                }

                override fun onFailure(call: Call<AddedReviewResponse>, t: Throwable) {
                    _postedReview.value = null
                }
            })
    }

}