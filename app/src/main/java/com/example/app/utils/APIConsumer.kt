package com.example.app.utils

import com.example.app.data.request.LoginRequest
import com.example.app.data.response.DashboardResponse
import com.example.app.data.response.LoginResponse
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
