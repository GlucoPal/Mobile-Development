package com.dicoding.glucopal.data.retrofit

import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.RegisterResponse
import com.dicoding.glucopal.utils.Gender
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: Gender
    ): RegisterResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}
