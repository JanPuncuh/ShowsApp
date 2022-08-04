package com.example.janinfinum

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("reviews") val reviews: ArrayList<Review2>,
    @SerialName("meta") val meta: Meta
)

@Entity(tableName = "review")
@Serializable
data class Review2(
    @PrimaryKey @SerialName("id") val id: String,
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Float,
    @SerialName("show_id") val showId: Int,
    @Embedded(prefix = "user_") @SerialName("user") val user: User
)