package com.dicoding.glucopal.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.LoginResult
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: Repository) : ViewModel() {

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: LoginResult) {
        viewModelScope.launch {
            repository.saveSession(user)
            Log.d("Hakiki", "Login Result loginviewmodel: $user")
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

 fun login(email:String, password:String) {
        viewModelScope.launch {
            try {
                _loginResponse.value = repository.login(email, password)
            } catch (e : Exception) {
                _loginResponse.value = LoginResponse(success = "0", message = "Terjadi Kesalahan.")
            }
        }
    }

}
