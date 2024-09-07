package com.example.app.repositories

import com.example.app.utils.APIConsumer
import com.example.app.utils.LoginRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor(private val consumer: APIConsumer) {
    // Login user and emit the result as a flow
    suspend fun loginUser(username: String, password: String) = flow {
        try {
            // Call the login endpoint
            val response = consumer.login(LoginRequest(username, password))
            // Check if the response is successful
            if (response.isSuccessful) {
                // Emit the keypass if the response body is not null
                response.body()?.let {
                    emit(Result.success(it.keypass))
                } ?: emit(Result.failure(Exception("Empty response body")))
            } else {
                // Emit an error if the response is not successful
                emit(Result.failure(Exception("Login failed: ${response.code()}")))
            }
        } catch (e: HttpException) {
            // Emit an error if there is a network error
            emit(Result.failure(Exception("Network error: ${e.message()}")))
        } catch (e: IOException) {
            // Emit an error if there is an IO error
            emit(Result.failure(Exception("IO error: ${e.message}")))
        }
    }
}