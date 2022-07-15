package com.example.janinfinum

import androidx.annotation.DrawableRes

data class Show(
    val title: String,
    val description: String,
    @DrawableRes val imageResourceId: Int
)