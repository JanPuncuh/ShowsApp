package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE showId=:id")
    fun getAllReviews(id: String): LiveData<List<Review2>>

    @Query("SELECT * FROM review WHERE id=:id")
    fun getReview(id: String): LiveData<Review2>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewReview(review: Review2)

}