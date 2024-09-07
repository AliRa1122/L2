package com.example.app.repositories

import com.example.app.utils.APIConsumer
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val consumer: APIConsumer) {
    // Get the dashboard data and emit the result as a flow
    suspend fun getDashboard(keypass: String) = flow {
        try {
            // Call the getDashboard endpoint
            val response = consumer.getDashboard(keypass)
            // Check if the response is successful
            if (response.isSuccessful) {
                response.body()?.let {
                    // Emit the dashboard response if the response body is not null
                    emit(Result.success(it))
                } ?: emit(Result.failure(Exception("Empty response body")))
            } else {
                // Emit an error if the response is not successful
                emit(Result.failure(Exception("Failed to fetch dashboard: ${response.code()}")))
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