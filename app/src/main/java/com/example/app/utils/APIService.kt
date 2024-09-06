package com.example.app.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIService {
    // Base URL
    private const val BASE_URL = "https://vu-nit3213-api.onrender.com/"

    // Create an OkHttpClient with custom timeouts
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Setting connection timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Setting read timeout
        .writeTimeout(30, TimeUnit.SECONDS)    // Setting write timeout
        .build()

    // Retrofit instance
    fun getService(): APIConsumer {
        // Create a Retrofit builder with the custom client
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Attach the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())

        // Build the Retrofit instance
        val retrofit: Retrofit = builder.build()
        // Create the APIConsumer interface
        return retrofit.create(APIConsumer::class.java)
    }
}
