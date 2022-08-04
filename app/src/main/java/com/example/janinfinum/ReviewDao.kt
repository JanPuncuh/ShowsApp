package com.example.janinfinum

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE showId=:id")
    fun getAllReviewsFromShow(id: String): LiveData<List<Review2>>

    @Query("SELECT * FROM review WHERE id=:id")
    fun getReview(id: String): LiveData<Review2>

    //todo its own Dao
    @Query("SELECT user_id as id, user_email as email, user_imageUrl as imageUrl FROM review WHERE user_email=:mail GROUP BY user_id")
    fun getUser(mail: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewReview(review: Review2): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviewsFromShow(reviews: List<Review2>): List<Long>

}