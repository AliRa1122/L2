package com.example.app.utils

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("footscray/auth")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val keypass: String
)