package com.example.lecom.service.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network client for creating Retrofit instances
 * Singleton pattern to ensure single instance across the app
 */
object NetworkClient {
    
    // Base URL - should be moved to BuildConfig or config file
    private const val BASE_URL = "https://api.example.com/" // Replace with your actual base URL
    
    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }
    
    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        
        // Add logging interceptor
        // Enable logging - you can disable this in release builds by checking BuildConfig.DEBUG
        // For now, we'll enable it. In production, uncomment the BuildConfig check below:
        // if (com.example.lecom.BuildConfig.DEBUG) { ... }
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(loggingInterceptor)
        
        // Add custom interceptors here (e.g., auth token interceptor)
        // builder.addInterceptor(AuthInterceptor())
        
        builder.build()
    }
    
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Create API service instance
     * @param T The service interface type
     * @return Instance of the service
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
    
    /**
     * Create API service instance (inline reified version)
     */
    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
    
    /**
     * Get base URL
     */
    fun getBaseUrl(): String = BASE_URL
    
    /**
     * Update base URL (useful for dynamic base URLs)
     */
    fun updateBaseUrl(newBaseUrl: String) {
        // Note: This would require recreating Retrofit instance
        // For dynamic URLs, consider using a different approach
    }
}

