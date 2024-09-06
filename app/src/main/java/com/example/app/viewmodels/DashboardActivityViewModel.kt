package com.example.app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.models.Entity
import com.example.app.repositories.DashboardRepository
import com.example.app.utils.DashboardResponse
import kotlinx.coroutines.launch

// ViewModel class responsible for managing UI-related data in a lifecycle-conscious way
class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {

    // LiveData to hold loading state, observing this will allow the UI to show/hide loading indicators
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to hold error messages, observing this will allow the UI to display error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // LiveData to hold the dashboard data fetched from the repository
    private val _dashboardData = MutableLiveData<List<Entity>>()
    val dashboardData: LiveData<List<Entity>> = _dashboardData

    // Function to fetch the dashboard data based on the provided keypass
    fun fetchDashboard(keypass: String) {
        // Launching a coroutine in the ViewModel's scope to perform asynchronous operations
        viewModelScope.launch {
            // Set loading state to true before making the network request
            _isLoading.value = true

            // Make the network request using the repository and collect the result
            repository.getDashboard(keypass).collect { result ->
                // Set loading state to false once the result is received
                _isLoading.value = false

                // Handle the result using fold function - success and failure cases
                result.fold(
                    onSuccess = { dashboardResponse ->
                        // Update LiveData with the fetched dashboard entities on success
                        _dashboardData.value = dashboardResponse.entities
                        _errorMessage.value = ""  // Clear any error message
                    },
                    onFailure = { error ->
                        // Update LiveData with the error message on failure
                        _errorMessage.value = error.message ?: "Unknown error occurred"
                        // Clear the dashboard data on failure
                        _dashboardData.value = emptyList()
                    }
                )
            }
        }
    }
}