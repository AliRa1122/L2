package com.example.app.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app.repositories.AuthRepository

class AuthFactory (
    // The factory receives the repository and application as parameters
    private val repository: AuthRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    // The create function is called when the ViewModel is created
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // If the model class is LoginActivityViewModel, return a new instance of LoginActivityViewModel
        if (modelClass.isAssignableFrom(LoginActivityViewModel::class.java)) {
            return LoginActivityViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
