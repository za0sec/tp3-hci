package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.LoginData
import com.example.app_grupo13.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    val loginResult = mutableStateOf<Boolean?>(null)
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loginUser(email: String, password: String) {
        Log.d("LoginViewModel", "Attempting login with email: $email")
        
        if (email.isBlank() || password.isBlank()) {
            Log.e("LoginViewModel", "Email or password is blank")
            loginResult.value = false
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Calling repository.loginUser")
                val result = loginRepository.loginUser(LoginData(email, password))
                Log.d("LoginViewModel", "Login result: $result")
                loginResult.value = result
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en login", e)
                loginResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
} 