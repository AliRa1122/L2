package com.example.app.utils

import com.example.app.models.Entity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// APIConsumer interface for Retrofit
interface APIConsumer {
    // Login endpoint
    @POST("footscray/auth")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    // Dashboard endpoint
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): Response<DashboardResponse>
}

// Data classes for API requests and responses
data class LoginRequest(
    val username: String,
    val password: String
)

// Data classes for API requests and responses
data class LoginResponse(
    val keypass: String
)

// Data classes for API requests and responses
data class DashboardResponse(
    val entities: List<Entity>,
    val entityTotal: Int
)
