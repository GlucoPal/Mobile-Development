package com.dicoding.glucopal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.LoginResult

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }

    /*fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }*/

}