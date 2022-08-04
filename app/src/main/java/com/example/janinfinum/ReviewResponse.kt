package com.example.janinfinum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("reviews") val reviews: ArrayList<Review2>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Review2(
    @SerialName("id") val id: String,
    @SerialName("comment") val comment: String?,
    @SerialName("rating") val rating: Float,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") val user: User
)