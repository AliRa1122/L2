package com.example.app.repositories

import com.example.app.utils.APIConsumer
import com.example.app.utils.DashboardResponse
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DashboardRepository(private val consumer: APIConsumer) {
    suspend fun getDashboard(keypass: String) = flow {
        try {
            val response = consumer.getDashboard(keypass)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty response body")))
            } else {
                emit(Result.failure(Exception("Failed to fetch dashboard: ${response.code()}")))
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error: ${e.message()}")))
        } catch (e: IOException) {
            emit(Result.failure(Exception("IO error: ${e.message}")))
        }
    }
}