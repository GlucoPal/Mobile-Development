package com.dicoding.glucopal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.LoginResponse
import com.dicoding.glucopal.data.response.LoginResult

class MainViewModel(private val repository: Repository) : ViewModel() {

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    /*fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }*/

}