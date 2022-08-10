package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

class ShowsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>> = _shows

    init {
        database.showsDao().getAllShows()
    }

    fun getShowsFromDatabase(): LiveData<List<Show>> {
        return database.showsDao().getAllShows()
    }

    fun onResponseAPI(shows: List<Show>) {
        _shows.value = shows
    }

    fun getShows(app: MyApplication) {
        ApiModule.retrofit.getShows("Bearer", app.token!!, app.client!!, app.uid!!)
            .enqueue(object : retrofit2.Callback<ShowResponse> {
                override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                    if (response.isSuccessful) {
                        onResponseAPI(response.body()?.shows!!)
                        app.database.showsDao().insertAllShows(response.body()?.shows!!)
                    }
                    //todo  handle unsuccessful
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {

                }
            })
    }

}