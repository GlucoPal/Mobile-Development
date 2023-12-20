package com.dicoding.glucopal.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.DetailResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private var _detailResponse = MutableLiveData<DetailResponse>()
    val detailResponse: LiveData<DetailResponse> = _detailResponse

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun getDetailHistory(resultId: Int) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _detailResponse.value = repository.getDetailHistory(resultId)
            } catch (e: Exception){
                _detailResponse.value = DetailResponse(success = 0)
            } finally {
                _loadingState.value = false
            }
        }
    }
}