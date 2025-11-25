package com.example.lecom.data.repository

import com.example.lecom.data.remote.dto.ApiResponse
import com.example.lecom.service.api.ApiService
import com.example.lecom.service.api.NetworkClient
import com.example.lecom.service.api.ExampleApiService
import com.example.lecom.service.api.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Generic API Repository
 * This class demonstrates how to use the ApiService from anywhere in the app
 * You can create similar repositories for different features
 */
class ApiRepository {
    
    // Initialize API service
    private val apiService: ExampleApiService = NetworkClient.createService()
    
    /**
     * Example: Get users using Result pattern
     */
    suspend fun getUsers(): Result<List<UserDto>> {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.getUsers()
        }
        return if (apiResponse.success && apiResponse.data != null) {
            Result.success(apiResponse.data)
        } else {
            Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            )
        }
    }
    
    /**
     * Example: Get users using ApiResponse wrapper
     */
    suspend fun getUsersWithResponse(): ApiResponse<List<UserDto>> {
        return ApiService.executeApiCallWithResponse {
            apiService.getUsers()
        }
    }
    
    /**
     * Example: Get users using Flow (for reactive programming)
     */
    fun getUsersFlow(): Flow<Result<List<UserDto>>> = flow {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.getUsers()
        }
        if (apiResponse.success && apiResponse.data != null) {
            emit(Result.success(apiResponse.data))
        } else {
            emit(Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            ))
        }
    }
    
    /**
     * Example: Get user by ID
     */
    suspend fun getUserById(userId: String): Result<UserDto> {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.getUserById(userId)
        }
        return if (apiResponse.success && apiResponse.data != null) {
            Result.success(apiResponse.data)
        } else {
            Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            )
        }
    }
    
    /**
     * Example: Create user
     */
    suspend fun createUser(user: UserDto): Result<UserDto> {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.createUser(user)
        }
        return if (apiResponse.success && apiResponse.data != null) {
            Result.success(apiResponse.data)
        } else {
            Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            )
        }
    }
    
    /**
     * Example: Update user
     */
    suspend fun updateUser(userId: String, user: UserDto): Result<UserDto> {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.updateUser(userId, user)
        }
        return if (apiResponse.success && apiResponse.data != null) {
            Result.success(apiResponse.data)
        } else {
            Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            )
        }
    }
    
    /**
     * Example: Delete user
     */
    suspend fun deleteUser(userId: String): Result<Unit> {
        val apiResponse = ApiService.executeApiCallWithResponse {
            apiService.deleteUser(userId)
        }
        return if (apiResponse.success) {
            Result.success(Unit)
        } else {
            Result.failure(
                Exception(apiResponse.error?.message ?: "Unknown error")
            )
        }
    }
}

/**
 * Usage example in ViewModel or any other class:
 * 
 * class MyViewModel : ViewModel() {
 *     private val repository = ApiRepository()
 *     
 *     fun loadUsers() {
 *         viewModelScope.launch {
 *             repository.getUsers()
 *                 .onSuccess { users ->
 *                     // Handle success
 *                 }
 *                 .onFailure { exception ->
 *                     // Handle error
 *                 }
 *         }
 *     }
 * }
 */

