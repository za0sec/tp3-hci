package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    
    private val _walletDetails = MutableStateFlow<WalletDetails?>(null)
    val walletDetails: StateFlow<WalletDetails?> = _walletDetails
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Cargar usuario y detalles de wallet en paralelo
                val userResponse = remoteDataSource.getUser()
                val walletResponse = remoteDataSource.getWalletDetails()

                if (userResponse.isSuccessful) {
                    _user.value = userResponse.body()
                } else {
                    _error.value = "Error al cargar los datos del usuario"
                    Log.e("ProfileViewModel", "Error: ${userResponse.code()}")
                }

                if (walletResponse.isSuccessful) {
                    _walletDetails.value = walletResponse.body()
                } else {
                    _error.value = "Error al cargar los detalles de la wallet"
                    Log.e("ProfileViewModel", "Error: ${walletResponse.code()}")
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
                Log.e("ProfileViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUser() {
        loadData()
    }

    suspend fun updateAlias(newAlias: String): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            
            val response = remoteDataSource.updateAlias(newAlias)
            if (response.isSuccessful) {
                _walletDetails.value = response.body()
                true
            } else {
                _error.value = "Error al actualizar el alias"
                Log.e("ProfileViewModel", "Error: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "Error desconocido"
            Log.e("ProfileViewModel", "Error updating alias", e)
            false
        } finally {
            _isLoading.value = false
        }
    }
} 