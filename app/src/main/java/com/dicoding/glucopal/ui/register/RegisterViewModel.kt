package com.dicoding.glucopal.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.RegisterResponse
import com.dicoding.glucopal.utils.Gender
import kotlinx.coroutines.launch

class RegisterViewModel (private val repository: Repository) : ViewModel() {

    private var _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse


    fun register(username: String, email:String, password:String, repeatPassword:String, gender: Gender) {
        viewModelScope.launch {
            try {
                _registerResponse.value = repository.register(username, email, password, repeatPassword, gender)
            } catch (e : Exception) {
                Log.e("RegisterViewModel", "Error during registration", e)
                _registerResponse.value = RegisterResponse(success = "0", message = "Terjadi Kesalahan.")
            }
        }
    }
}
