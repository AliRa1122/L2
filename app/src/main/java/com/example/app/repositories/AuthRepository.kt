package com.example.app.repositories

import com.example.app.utils.APIConsumer
import com.example.app.utils.LoginRequest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val consumer: APIConsumer) {
    suspend fun loginUser(username: String, password: String) = flow {
        try {
            val response = consumer.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it.keypass))
                } ?: emit(Result.failure(Exception("Empty response body")))
            } else {
                emit(Result.failure(Exception("Login failed: ${response.code()}")))
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error: ${e.message()}")))
        } catch (e: IOException) {
            emit(Result.failure(Exception("IO error: ${e.message}")))
        }
    }
}