package com.example.janinfinum

import androidx.annotation.DrawableRes

data class Show(
    val name: String,
    @DrawableRes val imageResourceId: Int
)