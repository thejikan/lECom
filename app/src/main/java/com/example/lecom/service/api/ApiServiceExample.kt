package com.example.lecom.service.api

import com.example.lecom.data.remote.dto.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Example API Service interface
 * Replace this with your actual API endpoints
 */
interface ExampleApiService : BaseApiService {
    
    // GET request example
    @GET("users")
    suspend fun getUsers(): Response<ApiResponse<List<UserDto>>>
    
    // GET request with path parameter
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<ApiResponse<UserDto>>
    
    // GET request with query parameters
    @GET("users")
    suspend fun getUsersByPage(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponse<List<UserDto>>>
    
    // POST request example
    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<ApiResponse<UserDto>>
    
    // PUT request example
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body user: UserDto
    ): Response<ApiResponse<UserDto>>
    
    // DELETE request example
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<ApiResponse<Unit>>
}

/**
 * Example DTO - Replace with your actual DTOs
 */
data class UserDto(
    val id: String? = null,
    val name: String,
    val email: String
)

