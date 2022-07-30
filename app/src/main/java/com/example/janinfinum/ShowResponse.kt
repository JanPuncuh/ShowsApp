package com.example.janinfinum

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ShowResponse(
    @SerialName("shows") val shows: List<Show2>,
    @SerialName("meta") val meta: Meta
)

@Entity(tableName = "show")
@Serializable
data class Show2(
    @PrimaryKey @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: Float?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("no_of_reviews") val no_of_reviews: Int,
    @SerialName("title") val title: String
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int,
)