package com.example.janinfinum

import androidx.annotation.DrawableRes
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*
import java.util.UUID.randomUUID

@Serializable
data class Show(
    @SerialName("title") val title: String,
    @SerialName("desc") val description: String,
    @SerialName("img") @DrawableRes val imageResourceId: Int,
    @SerialName("ID") @Contextual val ID: UUID = randomUUID()
) : DetailsItem