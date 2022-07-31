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

/*@Entity(
    tableName = "user",
    foreignKeys = [ForeignKey(entity = Review2::class, parentColumns = ["id"], childColumns = ["id"], onDelete = ForeignKey.CASCADE)]
)*/
@Entity(tableName = "user")
@Serializable
data class User(
    @PrimaryKey @SerialName("id") val userId: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") val image_url: String?
)