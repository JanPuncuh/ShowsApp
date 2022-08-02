package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShowsViewModel : ViewModel() {

    private val showsList = listOf(
        Show(
            "The Office",
            "The Office is an American mockumentary sitcom television series that depicts the everyday work lives of office employees in the Scranton, Pennsylvania, branch of the fictional Dunder Mifflin Paper Company. It aired on NBC from March 24, 2005, to May 16, 2013, lasting a total of nine seasons.",
            R.drawable.ic_office
        ),
        Show("Stranger Things", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.ic_stranger_things),
        Show("Krv Nije Voda", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", R.drawable.krv_nije_voda_1),
    )

    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>> = _shows

    init {
        _shows.value = showsList
    }

}