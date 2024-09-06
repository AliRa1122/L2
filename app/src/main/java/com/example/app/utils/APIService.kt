package com.example.app.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {
    private const val BASE_URL = "https://vu-nit3213-api.onrender.com/"

    // Retrofit instance
    fun getService(): APIConsumer {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)
    }
}
