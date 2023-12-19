package com.dicoding.glucopal.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.HistoryResponse
import com.dicoding.glucopal.data.response.LoginResult
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository): ViewModel() {
    private var _historyResponse = MutableLiveData<HistoryResponse>()
    val historyResponse: LiveData<HistoryResponse> = _historyResponse

    fun getHistory(userId: String) {
        viewModelScope.launch {
            try {
                _historyResponse.value = repository.getHistory(userId)
            } catch (e : Exception) {
                _historyResponse.value = HistoryResponse(success = 0)
            }
        }
    }
    fun getSession(): LiveData<LoginResult> {
        return repository.getSession().asLiveData()
    }
}