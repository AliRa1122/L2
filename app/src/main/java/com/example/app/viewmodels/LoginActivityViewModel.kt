package com.example.app.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginActivityViewModel(
    private val repository: AuthRepository,
    private val application: Application
) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var errorMessage: MutableLiveData<String?> = MutableLiveData()
    private var keypass: MutableLiveData<String?> = MutableLiveData()

    fun getIsLoading(): MutableLiveData<Boolean> = isLoading
    fun getErrorMessage(): MutableLiveData<String?> = errorMessage
    fun getKeyPass(): MutableLiveData<String?> = keypass

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            repository.loginUser(username, password).collect { result ->
                isLoading.value = false
                result.fold(
                    onSuccess = { keypassValue ->
                        keypass.value = keypassValue
                        errorMessage.value = ""
                    },
                    onFailure = { error ->
                        errorMessage.value = error.message ?: "Unknown error occurred"
                        keypass.value = null
                    }
                )
            }
        }
    }
}