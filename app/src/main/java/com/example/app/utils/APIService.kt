package com.example.app.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIService {
    // Base URL
    private const val BASE_URL = "https://vu-nit3213-api.onrender.com/"

    // Provide OkHttpClient
    @Provides
    @Singleton
    // OkHttpClient is a class for creating HTTP requests
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Provide Retrofit
    @Provides
    @Singleton
    // Retrofit is a class for creating REST API requests
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provide APIConsumer
    @Provides
    @Singleton
    fun provideAPIConsumer(retrofit: Retrofit): APIConsumer {
        return retrofit.create(APIConsumer::class.java)
    }
}
