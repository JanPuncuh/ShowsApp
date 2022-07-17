package com.example.janinfinum

import androidx.annotation.DrawableRes

data class Review(
    val username: String,
    val comment: String?,
    val rating: Float,
    @DrawableRes
    val profilePicture: Int
)