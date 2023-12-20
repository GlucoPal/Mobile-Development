package com.dicoding.glucopal.data

import com.dicoding.glucopal.data.pref.UserPreference
import com.dicoding.glucopal.data.response.CategoryResponse
import com.dicoding.glucopal.data.response.DetailResponse
import com.dicoding.glucopal.data.response.HistoryResponse
import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.LoginResult
import com.dicoding.glucopal.data.response.RegisterResponse
import com.dicoding.glucopal.data.response.UploadResponse
import com.dicoding.glucopal.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository (
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginData(email, password))
    }

    suspend fun register(username: String, email: String, password: String, repeatPassword: String): RegisterResponse {
        return apiService.register(RegistrationData(username, email, password, repeatPassword))
    }

    suspend fun getCategory(): CategoryResponse {
        return apiService.getCategory()
    }

    suspend fun upload(userId: String, image: MultipartBody.Part, food_name: RequestBody, idFood: RequestBody, GI: RequestBody): UploadResponse {
        return apiService.uploadImage(userId, image, food_name, idFood, GI)
    }

    suspend fun getHistory(userId: String): HistoryResponse {
        return apiService.getHistory(userId)
    }

    suspend fun getDetailHistory(resultId: Int): DetailResponse {
        return apiService.getDetailHistory(resultId)
    }

    suspend fun deleteHistoryItem(resultId: Int) {
        return apiService.deleteHistoryItem(resultId)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}
