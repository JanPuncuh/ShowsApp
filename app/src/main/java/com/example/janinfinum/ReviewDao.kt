package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {


    /*@Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User)


    @Query("SELECT * FROM review")
    fun getAllReviews(): LiveData<List<Review2>>

    @Query("SELECT * FROM review WHERE id=:id")
    fun getReview(id: String): LiveData<Review2>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewReview(shows : Review2)
*/
}