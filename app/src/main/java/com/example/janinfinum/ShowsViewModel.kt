package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _shows = MutableLiveData<List<Show2>>()
    val shows: LiveData<List<Show2>> = _shows

    init {
        database.showsDao().getAllShows()
    }

    fun getShowsFromDatabase(): LiveData<List<Show2>> {
        return database.showsDao().getAllShows()
    }

    fun onResponseAPI(shows: List<Show2>) {
        _shows.value = shows
    }

}