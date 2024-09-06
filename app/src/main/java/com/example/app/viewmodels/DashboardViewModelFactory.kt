package com.example.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app.repositories.DashboardRepository

// Factory class for creating an instance of DashboardViewModel with a custom constructor
class DashboardViewModelFactory(private val repository: DashboardRepository) : ViewModelProvider.Factory {

    // Overriding create function to instantiate DashboardViewModel with the provided repository
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the requested ViewModel class is assignable from DashboardViewModel
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Return a new instance of DashboardViewModel
            return DashboardViewModel(repository) as T
        }
        // Throw an exception if the ViewModel class is unknown
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}