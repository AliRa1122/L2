package com.example.app.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {
    // Base URL
    private const val BASE_URL = "https://vu-nit3213-api.onrender.com/"

    // Retrofit instance
    fun getService(): APIConsumer {
        // Create a Retrofit builder
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        // Build the Retrofit instance
        val retrofit: Retrofit = builder.build()
        // Create the APIConsumer interface
        return retrofit.create(APIConsumer::class.java)
    }
}
