# API Usage Guide

This guide explains how to use the API structure from anywhere in your app.

## Overview

The API structure consists of:
- **NetworkClient**: Retrofit setup and configuration
- **ApiService**: Generic API call executor with error handling
- **BaseApiService**: Base interface for all API services
- **ApiException**: Custom exception classes for error handling
- **ApiResponse**: Generic response wrapper

## Step 1: Create Your API Service Interface

Create an interface that extends `BaseApiService` and define your endpoints:

```kotlin
interface UserApiService : BaseApiService {
    @GET("users")
    suspend fun getUsers(): Response<ApiResponse<List<UserDto>>>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): Response<ApiResponse<UserDto>>
    
    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<ApiResponse<UserDto>>
}
```

## Step 2: Initialize Your API Service

```kotlin
// Option 1: Using NetworkClient
private val userApiService: UserApiService = NetworkClient.createService()

// Option 2: Using inline reified (cleaner)
private val userApiService = NetworkClient.createService<UserApiService>()
```

## Step 3: Make API Calls

### Method 1: Using Result Pattern (Recommended)

```kotlin
class UserRepository {
    private val apiService = NetworkClient.createService<UserApiService>()
    
    suspend fun getUsers(): Result<List<UserDto>> {
        return ApiService.executeApiCall {
            apiService.getUsers()
        }
    }
}

// Usage in ViewModel or Fragment
viewModelScope.launch {
    repository.getUsers()
        .onSuccess { users ->
            // Handle success
            _users.value = users
        }
        .onFailure { exception ->
            // Handle error
            when (exception) {
                is ApiException.NetworkException -> {
                    // Show network error message
                }
                is ApiException.UnauthorizedException -> {
                    // Handle unauthorized
                }
                else -> {
                    // Handle other errors
                }
            }
        }
}
```

### Method 2: Using ApiResponse Wrapper

```kotlin
suspend fun getUsersWithResponse(): ApiResponse<List<UserDto>> {
    return ApiService.executeApiCallWithResponse {
        apiService.getUsers()
    }
}

// Usage
viewModelScope.launch {
    val response = repository.getUsersWithResponse()
    if (response.success) {
        response.data?.let { users ->
            // Handle success
        }
    } else {
        response.error?.let { error ->
            // Handle error
        }
    }
}
```

### Method 3: Using Flow (Reactive)

```kotlin
fun getUsersFlow(): Flow<Result<List<UserDto>>> = flow {
    emit(ApiService.executeApiCall {
        apiService.getUsers()
    })
}

// Usage in ViewModel
fun loadUsers() {
    viewModelScope.launch {
        repository.getUsersFlow()
            .collect { result ->
                result.onSuccess { users ->
                    _users.value = users
                }.onFailure { exception ->
                    _error.value = exception.message
                }
            }
    }
}
```

### Method 4: Direct Call with Exception (Use with try-catch)

```kotlin
suspend fun getUserById(userId: String): UserDto {
    return ApiService.executeApiCallOrThrow {
        apiService.getUserById(userId)
    }
}

// Usage
viewModelScope.launch {
    try {
        val user = repository.getUserById("123")
        // Handle success
    } catch (e: ApiException) {
        // Handle error
        when (e) {
            is ApiException.NotFoundException -> { /* ... */ }
            is ApiException.NetworkException -> { /* ... */ }
            else -> { /* ... */ }
        }
    }
}
```

## Usage in Different Components

### In ViewModel

```kotlin
class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            repository.getUsers()
                .onSuccess { users ->
                    _users.value = users
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }
}
```

### In Fragment/Activity

```kotlin
class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collect { users ->
                // Update UI
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    // Show error message
                }
            }
        }
        
        viewModel.loadUsers()
    }
}
```

### In Repository

```kotlin
class UserRepository {
    private val apiService = NetworkClient.createService<UserApiService>()
    
    suspend fun getAllUsers(): Result<List<UserDto>> {
        return ApiService.executeApiCall {
            apiService.getUsers()
        }
    }
    
    suspend fun getUserById(id: String): Result<UserDto> {
        return ApiService.executeApiCall {
            apiService.getUserById(id)
        }
    }
    
    suspend fun createUser(user: UserDto): Result<UserDto> {
        return ApiService.executeApiCall {
            apiService.createUser(user)
        }
    }
}
```

### In UseCase (Domain Layer)

```kotlin
class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getAllUsers().map { dtos ->
            dtos.map { it.toDomain() }
        }
    }
}
```

## Error Handling

The API structure provides comprehensive error handling:

```kotlin
when (exception) {
    is ApiException.NetworkException -> {
        // No internet connection or network error
    }
    is ApiException.UnauthorizedException -> {
        // 401 - Unauthorized
    }
    is ApiException.NotFoundException -> {
        // 404 - Resource not found
    }
    is ApiException.TimeoutException -> {
        // Request timeout
    }
    is ApiException.ServerException -> {
        // 5xx - Server error
    }
    is ApiException.HttpException -> {
        // Other HTTP errors
        val code = exception.code
    }
    is ApiException.ParseException -> {
        // JSON parsing error
    }
    is ApiException.UnknownException -> {
        // Unknown error
    }
}
```

## Configuration

### Update Base URL

Edit `NetworkClient.kt` and update the `BASE_URL` constant:

```kotlin
private const val BASE_URL = "https://your-api-url.com/"
```

### Add Custom Interceptors

In `NetworkClient.kt`, you can add custom interceptors:

```kotlin
// Example: Auth Token Interceptor
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}

// Add to OkHttpClient
builder.addInterceptor(AuthInterceptor())
```

## Best Practices

1. **Always use Result pattern** for better error handling
2. **Use suspend functions** for API calls
3. **Handle errors appropriately** in UI layer
4. **Use Flow** for reactive data streams
5. **Keep API services in `service/api/`** folder
6. **Keep DTOs in `data/remote/dto/`** folder
7. **Use repositories** to abstract API calls from ViewModels

## Example: Complete Flow

```kotlin
// 1. Define API Service
interface ProductApiService : BaseApiService {
    @GET("products")
    suspend fun getProducts(): Response<ApiResponse<List<ProductDto>>>
}

// 2. Create Repository
class ProductRepository {
    private val apiService = NetworkClient.createService<ProductApiService>()
    
    suspend fun getProducts(): Result<List<ProductDto>> {
        return ApiService.executeApiCall {
            apiService.getProducts()
        }
    }
}

// 3. Use in ViewModel
class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    
    fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts()
                .onSuccess { products ->
                    // Update UI state
                }
                .onFailure { exception ->
                    // Handle error
                }
        }
    }
}
```

