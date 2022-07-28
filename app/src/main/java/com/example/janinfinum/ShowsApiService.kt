package com.example.janinfinum


import retrofit2.Call
import retrofit2.http.*

interface ShowsApiService {
    //users
    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    //shows
    @GET("/shows")
    fun getShows(
        @Header("token-type") tokenType: String, @Header("access-token") auth: String,
        @Header("client") client: String, @Header("uid") mail: String
    ): Call<ShowResponse>

    @GET("/shows/{id}")
    fun getShow(
        @Path("id") searchById: String,
        @Header("token-type") tokenType: String, @Header("access-token") auth: String,
        @Header("client") client: String, @Header("uid") mail: String
    ): Call<ShowDetailsResponse>

    //reviews
    @POST("/users")
    fun postReview(@Body request: ReviewRequest): Call<ReviewResponse>

}