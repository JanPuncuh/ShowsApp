package com.example.janinfinum

import androidx.annotation.DrawableRes
import java.util.*
import java.util.UUID.randomUUID

data class Show(
    val title: String,
    val description: String,
    @DrawableRes val imageResourceId: Int,
    val ID: UUID = randomUUID()
)