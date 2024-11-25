package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecoverPasswordViewModel(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    suspend fun recoverPassword(email: String): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            _email.value = email
            
            val response = remoteDataSource.recoverPassword(email)
            if (response.isSuccessful) {
                true
            } else {
                _error.value = "Error al enviar el correo de recuperación"
                Log.e("RecoverPasswordViewModel", "Error: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "Error desconocido"
            Log.e("RecoverPasswordViewModel", "Error recovering password", e)
            false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun resetPassword(code: String, newPassword: String): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            
            val response = remoteDataSource.resetPassword(code, newPassword)
            if (response.isSuccessful) {
                true
            } else {
                _error.value = "Error al restablecer la contraseña"
                Log.e("RecoverPasswordViewModel", "Error: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "Error desconocido"
            Log.e("RecoverPasswordViewModel", "Error resetting password", e)
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun setEmail(email: String) {
        _email.value = email
    }
} 