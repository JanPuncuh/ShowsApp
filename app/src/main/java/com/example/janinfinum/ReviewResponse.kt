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

/*@Entity(
    tableName = "review",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["id"], onDelete = CASCADE)]
)*/

@Entity(tableName = "review")
@Serializable
data class Review2(
    @PrimaryKey @SerialName("id") val id: String, //primary key
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Float,
    @SerialName("show_id") val imageUrl: Int,
    @Embedded @SerialName("user") val user: User //foreign key
)