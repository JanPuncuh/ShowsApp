package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowsDao {

    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<Show2>>

    @Query("SELECT * FROM show WHERE id=:id")
    fun getShow(id: String): LiveData<Show2>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows : List<Show2>)

}