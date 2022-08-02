package com.example.janinfinum

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("user") val user: User
)

@Serializable
data class User(
    @PrimaryKey @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") val imageUrl: String?
)