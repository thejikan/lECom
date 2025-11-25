package com.example.lecom.service.api

import com.example.lecom.data.remote.dto.ApiError
import com.example.lecom.data.remote.dto.ApiResponse
import com.example.lecom.utils.ApiException
import com.example.lecom.utils.toApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * Base API Service interface
 * All API service interfaces should extend this
 */
interface BaseApiService {
    // Common API endpoints can be defined here
}

/**
 * Generic API Repository/Service class for making API calls
 * Can be used from anywhere in the app
 */
object ApiService {
    
    /**
     * Execute API call and handle response
     * @param apiCall The suspend function that makes the API call
     * @return Result containing either success data or exception
     */
    suspend fun <T> executeApiCall(
        apiCall: suspend () -> Response<T>
    ): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(ApiException.ParseException("Response body is null"))
                    }
                } else {
                    Result.failure(handleHttpError(response.code(), response.message()))
                }
            } catch (e: IOException) {
                Result.failure(e.toApiException())
            } catch (e: Exception) {
                Result.failure(ApiException.UnknownException(e.message ?: "Unknown error"))
            }
        }
    }
    
    /**
     * Execute API call with ApiResponse wrapper
     * @param apiCall The suspend function that makes the API call
     * @return ApiResponse containing success status and data/error
     */
    suspend fun <T> executeApiCallWithResponse(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): ApiResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        body
                    } else {
                        ApiResponse(
                            success = false,
                            error = ApiError(message = "Response body is null")
                        )
                    }
                } else {
                    ApiResponse(
                        success = false,
                        error = ApiError(
                            code = response.code(),
                            message = response.message()
                        )
                    )
                }
            } catch (e: IOException) {
                ApiResponse(
                    success = false,
                    error = ApiError(message = e.toApiException().message)
                )
            } catch (e: Exception) {
                ApiResponse(
                    success = false,
                    error = ApiError(message = e.message ?: "Unknown error")
                )
            }
        }
    }
    
    /**
     * Execute API call and return data directly (throws exception on error)
     * @param apiCall The suspend function that makes the API call
     * @return The response data
     * @throws ApiException if the call fails
     */
    suspend fun <T> executeApiCallOrThrow(
        apiCall: suspend () -> Response<T>
    ): T {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        body
                    } else {
                        throw ApiException.ParseException("Response body is null")
                    }
                } else {
                    throw handleHttpError(response.code(), response.message())
                }
            } catch (e: IOException) {
                throw e.toApiException()
            } catch (e: ApiException) {
                throw e
            } catch (e: Exception) {
                throw ApiException.UnknownException(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Handle HTTP error codes
     */
    private fun handleHttpError(code: Int, message: String?): ApiException {
        return when (code) {
            401 -> ApiException.UnauthorizedException(message ?: "Unauthorized")
            404 -> ApiException.NotFoundException(message ?: "Not found")
            408 -> ApiException.TimeoutException(message ?: "Request timeout")
            in 500..599 -> ApiException.ServerException(message ?: "Server error")
            else -> ApiException.HttpException(code, message ?: "HTTP error")
        }
    }
}

