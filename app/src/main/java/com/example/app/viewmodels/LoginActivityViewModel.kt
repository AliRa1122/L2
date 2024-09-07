package com.example.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginActivityViewModel(
    // The ViewModel receives the repository and application as parameters
    private val repository: AuthRepository,
) : ViewModel() {
    // The isLoading variable is a MutableLiveData that stores a boolean value
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    // The errorMessage variable is a MutableLiveData that stores a string value
    private var errorMessage: MutableLiveData<String?> = MutableLiveData()
    // The keypass variable is a MutableLiveData that stores a string value
    private var keypass: MutableLiveData<String?> = MutableLiveData()

    // Getters for the isLoading, errorMessage, and keypass variables
    fun getIsLoading(): MutableLiveData<Boolean> = isLoading
    fun getErrorMessage(): MutableLiveData<String?> = errorMessage
    fun getKeyPass(): MutableLiveData<String?> = keypass

    // The loginUser function is called from the LoginActivity
    fun loginUser(username: String, password: String) {
        // The viewModelScope.launch block is used to launch a coroutine
        viewModelScope.launch {
            // Set the value of isLoading to true
            isLoading.value = true
            // Call the loginUser function from the repository passing the username and password
            repository.loginUser(username, password).collect { result ->
                isLoading.value = false
                // The result is a sealed class that can be either a success or a failure
                result.fold(
                    // If the result is a success, set the value of keypass to the keypassValue
                    onSuccess = { keypassValue ->
                        keypass.value = keypassValue
                        errorMessage.value = ""
                    },
                    // If the result is a failure, set the value of errorMessage to the error message
                    onFailure = { error ->
                        errorMessage.value = error.message ?: "Unknown error occurred"
                        keypass.value = null
                    }
                )
            }
        }
    }
}