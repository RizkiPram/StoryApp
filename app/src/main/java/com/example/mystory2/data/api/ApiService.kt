package com.example.mystory2.data.api

import com.example.mystory2.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/v1/register")
    fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @Multipart
    @POST("/v1/stories")
    fun addStories(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
    ): Call<AddResponse>

    @GET("/v1/stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") bearer: String?
    ): StoryResponse

    @GET("/v1/stories")
    fun getMaps(
        @Header("Authorization") bearer: String?,
        @Query("location") page: Int,
    ): Call<StoryMapsResponse>
}