package com.dicoding.glucopal.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.UploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel (private val repository: Repository) : ViewModel() {
    private var _uploadResponse = MutableLiveData<UploadResponse?>()
    val uploadResponse: LiveData<UploadResponse?> = _uploadResponse

    fun upload(userId: Int, image: MultipartBody.Part, food_name: RequestBody, idFood: RequestBody, GI: RequestBody) {
        viewModelScope.launch {
            try {
                val uploadImageResponse = repository.upload(userId, image, food_name, idFood, GI)
                _uploadResponse.value = uploadImageResponse
            } catch (e: Exception) {
                _uploadResponse.value = UploadResponse(
                    success = 0
                )
            }
        }
    }
}