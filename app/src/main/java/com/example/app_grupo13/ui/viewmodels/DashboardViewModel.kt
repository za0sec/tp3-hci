package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.repository.UserRepository
import kotlinx.coroutines.launch

class DashboardViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user
    
    private val _balance = mutableStateOf<Double?>(null)
    val balance: State<Double?> = _balance
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchUserData()
        fetchBalance()
    }

    private fun fetchUserData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val success = userRepository.fetchUser()
                if (success) {
                    _user.value = userRepository.getCurrentUser()
                    Log.d("DashboardViewModel", "User data fetched: ${_user.value}")
                } else {
                    Log.e("DashboardViewModel", "Failed to fetch user data")
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching user data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchBalance() {
        viewModelScope.launch {
            try {
                val response = userRepository.fetchBalance()
                if (response.isSuccessful && response.body() != null) {
                    _balance.value = response.body()?.balance
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching balance", e)
            }
        }
    }
} 