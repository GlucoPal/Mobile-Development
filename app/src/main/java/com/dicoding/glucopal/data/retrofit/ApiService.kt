package com.dicoding.glucopal.data.retrofit

import com.dicoding.glucopal.data.LoginData
import com.dicoding.glucopal.data.RegistrationData
import com.dicoding.glucopal.data.response.CategoryResponse
import com.dicoding.glucopal.data.response.DetailResponse
import com.dicoding.glucopal.data.response.HistoryResponse
import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.RegisterResponse
import com.dicoding.glucopal.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("api/register")
    suspend fun register(@Body registrationData: RegistrationData): RegisterResponse

    @POST("api/login")
    suspend fun login(@Body loginData: LoginData): LoginResponse

    @GET("api/allfood")
    suspend fun getCategory(): CategoryResponse

    @Multipart
    @POST("api/scan/{idUser}")
    suspend fun uploadImage(
        @Path("idUser") userId: String,
        @Part image: MultipartBody.Part,
        @Part("food_name") foodName: RequestBody,
        @Part("idFood") idFood: RequestBody,
        @Part("GI") GI: RequestBody
    ): UploadResponse

    @GET("api/history/{idUser}")
    suspend fun getHistory(
        @Path("idUser") userId: String
    ): HistoryResponse

    @GET("api/result/{idResult}")
    suspend fun getDetailHistory(
        @Path("idResult") resultId: Int
    ): DetailResponse

    @DELETE("api/history/{idResult}")
    suspend fun deleteHistoryItem(
        @Path("idResult") resultId: Int
    )
}
