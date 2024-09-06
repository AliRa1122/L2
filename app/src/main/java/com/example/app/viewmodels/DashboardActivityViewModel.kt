package com.example.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.models.Entity
import com.example.app.repositories.DashboardRepository
import com.example.app.utils.DashboardResponse
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _dashboardData = MutableLiveData<List<Entity>>()
    val dashboardData: LiveData<List<Entity>> = _dashboardData

    fun fetchDashboard(keypass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getDashboard(keypass).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { dashboardResponse ->
                        _dashboardData.value = dashboardResponse.entities
                        _errorMessage.value = ""
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Unknown error occurred"
                        _dashboardData.value = emptyList()
                    }
                )
            }
        }
    }
}