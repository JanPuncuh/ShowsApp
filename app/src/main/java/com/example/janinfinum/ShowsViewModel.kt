package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel(private val database: AppDatabase) : ViewModel() {

    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>> = _shows

    private val _shows2 = MutableLiveData<List<Show2>>()
    val shows2: LiveData<List<Show2>> = _shows2

    init {
        database.showsDao().getAllShows()
    }

    fun getShowsFromDatabase(): LiveData<List<Show2>> {
        _shows2.value = database.showsDao().getAllShows().value
        return _shows2
    }

    fun saveShowsToDatabase(shows: List<Show2>) {
        database.showsDao().insertAllShows(shows)
    }

    fun onResponseAPI(shows: List<Show2>) {
        _shows2.value = shows
    }

}