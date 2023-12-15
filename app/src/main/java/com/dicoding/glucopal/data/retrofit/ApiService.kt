package com.dicoding.glucopal.data.retrofit

import com.dicoding.glucopal.data.LoginData
import com.dicoding.glucopal.data.RegistrationData
import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/register")
    suspend fun register(@Body registrationData: RegistrationData): RegisterResponse

    @POST("api/login")
    suspend fun login(@Body loginData: LoginData): LoginResponse
}
