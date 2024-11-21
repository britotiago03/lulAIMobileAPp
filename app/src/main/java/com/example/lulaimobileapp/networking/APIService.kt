package com.example.lulaimobileapp.networking

import android.util.Log
import com.example.lulaimobileapp.utils.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object APIService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(APIEndpoints::class.java)

    // Register endpoint
    suspend fun register(username: String, email: String, password: String): Pair<Boolean, String?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(RegisterRequest(username, email, password))
                if (response.isSuccessful && response.body() != null) {
                    Pair(true, response.body()?.token)
                } else {
                    Log.e("APIService", "Register failed: ${response.errorBody()?.string()}")
                    Pair(false, "Failed to register. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("APIService", "Register exception: ${e.message}", e)
                Pair(false, "An error occurred. Please try again.")
            }
        }
    }

    suspend fun login(email: String, password: String): Pair<Boolean, String?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    Pair(true, response.body()?.token)
                } else {
                    Log.e("APIService", "Login failed: ${response.errorBody()?.string()}")
                    Pair(false, "Invalid email or password.")
                }
            } catch (e: Exception) {
                Log.e("APIService", "Login exception: ${e.message}", e)
                Pair(false, "An error occurred. Please try again.")
            }
        }
    }

    suspend fun fetchUser(token: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.fetchUser("Bearer $token")
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("APIService", "Fetch user failed: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("APIService", "Fetch user exception: ${e.message}", e)
                null
            }
        }
    }

    suspend fun updateUserProfile(token: String, username: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateUserProfile("Bearer $token", UpdateUserProfileRequest(username))
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("APIService", "Update profile exception: ${e.message}", e)
                false
            }
        }
    }

    suspend fun deleteUser(token: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteUser("Bearer $token")
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("APIService", "Delete user exception: ${e.message}", e)
                false
            }
        }
    }

    suspend fun verifyOTP(email: String, otp: String): Pair<Boolean, String?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.verifyOTP(OTPRequest(email, otp))
                if (response.isSuccessful) {
                    Pair(true, "OTP verified")
                } else {
                    Log.e("APIService", "Verify OTP failed: ${response.errorBody()?.string()}")
                    Pair(false, "Invalid OTP.")
                }
            } catch (e: Exception) {
                Log.e("APIService", "Verify OTP exception: ${e.message}", e)
                Pair(false, "An error occurred. Please try again.")
            }
        }
    }

    suspend fun forgotPassword(email: String): Pair<Boolean, String?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.forgotPassword(ForgotPasswordRequest(email))
                if (response.isSuccessful) {
                    Pair(true, "OTP sent to your email.")
                } else {
                    Log.e("APIService", "Forgot password failed: ${response.errorBody()?.string()}")
                    Pair(false, "Failed to send OTP. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("APIService", "Forgot password exception: ${e.message}", e)
                Pair(false, "An error occurred. Please try again.")
            }
        }
    }
}

interface APIEndpoints {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<RegisterResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): retrofit2.Response<LoginResponse>

    @GET("/api/user/profile")
    suspend fun fetchUser(@Header("Authorization") token: String): retrofit2.Response<User>

    @PUT("/api/user/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateUserProfileRequest
    ): retrofit2.Response<Unit>

    @DELETE("/api/user")
    suspend fun deleteUser(@Header("Authorization") token: String): retrofit2.Response<Unit>

    @POST("/api/auth/verify-otp")
    suspend fun verifyOTP(@Body request: OTPRequest): retrofit2.Response<Unit>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): retrofit2.Response<Unit>
}

// Data models
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("token") val token: String
)

data class LoginRequest(
    @SerializedName("UserEmail") val email: String,
    @SerializedName("UserPassword") val password: String
)

data class LoginResponse(
    val token: String
)

data class OTPRequest(
    @SerializedName("email") val email: String,
    @SerializedName("otp") val otp: String
)

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

data class UpdateUserProfileRequest(
    @SerializedName("username") val username: String
)
