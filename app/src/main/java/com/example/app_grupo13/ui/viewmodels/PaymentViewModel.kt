package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.Payment
import com.example.app_grupo13.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(
    private val repository: PaymentRepository
) : ViewModel() {
    
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _success = MutableStateFlow<Boolean?>(null)
    val success: StateFlow<Boolean?> = _success

    suspend fun makePayment(amount: Double, receiverEmail: String): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            _success.value = null
            
            repository.makePayment(amount, receiverEmail).fold(
                onSuccess = { 
                    _success.value = true
                    true
                },
                onFailure = { e ->
                    _error.value = e.message
                    _success.value = false
                    false
                }
            )
        } catch (e: Exception) {
            _error.value = e.message ?: "Error al realizar la transferencia"
            _success.value = false
            Log.e("PaymentViewModel", "Error making payment", e)
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun loadPayments() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                repository.getPayments()?.let { paymentList ->
                    _payments.value = paymentList
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los pagos"
                Log.e("PaymentViewModel", "Error loading payments", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
} 