package com.example.app_grupo13.data.repository

import android.util.Log
import com.example.app_grupo13.data.model.Balance
import com.example.app_grupo13.data.model.RegistrationData
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response

class UserRepository(private val remoteDataSource: RemoteDataSource) {
    suspend fun registerUser(registrationData: RegistrationData): Boolean {
        return try {
            remoteDataSource.registerUser(registrationData)
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error en registro", e)
            false
        }
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun fetchUser(): Boolean {
        return try {
            Log.d("UserRepository", "Fetching user data")
            val response = remoteDataSource.getUser()
            
            if (response.isSuccessful && response.body() != null) {
                _currentUser.value = response.body()
                Log.d("UserRepository", "User data fetched successfully: ${_currentUser.value}")
                true
            } else {
                Log.e("UserRepository", "Error fetching user: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception fetching user", e)
            false
        }
    }

    fun getCurrentUser(): User? = _currentUser.value

    suspend fun fetchBalance(): Response<Balance> {
        return remoteDataSource.getBalance()
    }

    suspend fun fetchWalletDetails(): Response<WalletDetails> {
        return remoteDataSource.getWalletDetails()
    }
}
