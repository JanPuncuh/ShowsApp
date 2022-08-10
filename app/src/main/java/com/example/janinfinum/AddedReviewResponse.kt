package com.example.janinfinum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddedReviewResponse(
    @SerialName("review") val review: Review,
)