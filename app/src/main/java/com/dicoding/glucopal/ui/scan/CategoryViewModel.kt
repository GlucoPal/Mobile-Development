package com.dicoding.glucopal.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.glucopal.data.Repository
import com.dicoding.glucopal.data.response.CategoryResponse
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepository: Repository) : ViewModel() {
    private var _categoryResponse = MutableLiveData<CategoryResponse>()
    val categoryResponse: LiveData<CategoryResponse> = _categoryResponse

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun getCategory() {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _categoryResponse.value = categoryRepository.getCategory()
            } catch (e : Exception) {
                _categoryResponse.value = CategoryResponse(success = 0)
            } finally {
                _loadingState.value = false
            }
        }
    }
}
