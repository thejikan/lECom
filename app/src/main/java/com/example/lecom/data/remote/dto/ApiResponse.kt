package com.example.lecom.data.remote.dto

/**
 * Generic API Response wrapper
 * @param T The type of data in the response
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: ApiError? = null
)

/**
 * API Error model
 */
data class ApiError(
    val code: Int? = null,
    val message: String? = null,
    val details: Map<String, String>? = null
)

/**
 * Base DTO interface for all API responses
 */
interface BaseDto {
    fun toDomain(): Any?
}

