package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepositViewModel(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {
    
    private val _walletDetails = MutableStateFlow<WalletDetails?>(null)
    val walletDetails: StateFlow<WalletDetails?> = _walletDetails
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    suspend fun rechargeWallet(amount: Double): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            
            val response = remoteDataSource.rechargeWallet(amount)
            if (response.isSuccessful) {
                _walletDetails.value = response.body()
                true
            } else {
                _error.value = "Error al realizar el depósito"
                Log.e("DepositViewModel", "Error: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            _error.value = e.message ?: "Error desconocido"
            Log.e("DepositViewModel", "Error recharging wallet", e)
            false
        } finally {
            _isLoading.value = false
        }
    }
} 