package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowsDao {

    @Query("SELECT * FROM show")
    fun getAllShows(): LiveData<List<Show>>

    @Query("SELECT * FROM show WHERE id=:id")
    fun getShow(id: String): LiveData<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows : List<Show>)

}