package com.example.lecom.utils

import java.io.IOException

/**
 * Base exception class for API errors
 */
sealed class ApiException(message: String) : Exception(message) {
    class NetworkException(message: String = "Network error occurred") : ApiException(message)
    class HttpException(val code: Int, message: String) : ApiException(message)
    class ParseException(message: String = "Failed to parse response") : ApiException(message)
    class UnknownException(message: String = "Unknown error occurred") : ApiException(message)
    class TimeoutException(message: String = "Request timeout") : ApiException(message)
    class UnauthorizedException(message: String = "Unauthorized access") : ApiException(message)
    class NotFoundException(message: String = "Resource not found") : ApiException(message)
    class ServerException(message: String = "Server error") : ApiException(message)
}

/**
 * Extension function to convert IOException to ApiException
 */
fun IOException.toApiException(): ApiException {
    return when {
        message?.contains("timeout", ignoreCase = true) == true -> 
            ApiException.TimeoutException(message ?: "Request timeout")
        message?.contains("Unable to resolve host", ignoreCase = true) == true -> 
            ApiException.NetworkException("No internet connection")
        else -> 
            ApiException.NetworkException(message ?: "Network error occurred")
    }
}

