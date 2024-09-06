package com.example.app.utils

import com.example.app.models.Entity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIConsumer {
    @POST("footscray/auth")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): Response<DashboardResponse>
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)

data class DashboardResponse(
    val entities: List<Entity>,
    val entityTotal: Int
)
