package com.example.app_grupo13.data.repository

import android.util.Log
import com.example.app_grupo13.data.local.SessionManager
import com.example.app_grupo13.data.model.LoginData
import com.example.app_grupo13.data.network.RemoteDataSource

class LoginRepository(
    private val remoteDataSource: RemoteDataSource,
    private val sessionManager: SessionManager
) {
    suspend fun loginUser(loginData: LoginData): Boolean {
        return try {
            Log.d("LoginRepository", "Attempting to login with RemoteDataSource")
            val response = remoteDataSource.loginUser(loginData)
            Log.d("LoginRepository", "Response received: ${response.isSuccessful}, Code: ${response.code()}")
            
            when {
                response.isSuccessful && response.body() != null -> {
                    val token = response.body()?.token
                    if (token != null) {
                        Log.d("LoginRepository", "Token received: ${token.take(10)}...")
                        sessionManager.saveAuthToken(token)
                        true
                    } else {
                        Log.e("LoginRepository", "Token was null in response body")
                        false
                    }
                }
                response.code() == 401 -> {
                    Log.e("LoginRepository", "Unauthorized: Invalid credentials")
                    false
                }
                else -> {
                    Log.e("LoginRepository", "Login failed with code ${response.code()}: ${response.errorBody()?.string()}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("LoginRepository", "Exception during login", e)
            false
        }
    }
} 