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

    private var isLastPage = false
    private var currentPage = 1
    private var isLoadingMore = false

    fun loadPayments(page: Int = 1) {
        if (_isLoading.value || (page != 1 && isLastPage)) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                currentPage = page
                
                repository.getPayments(page = page)?.let { paymentList ->
                    if (page == 1) {
                        _payments.value = paymentList
                    } else {
                        _payments.value = _payments.value + paymentList
                    }
                    isLastPage = paymentList.isEmpty()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al cargar los pagos"
                Log.e("PaymentViewModel", "Error loading payments", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMorePayments() {
        if (!_isLoading.value && !isLastPage && !isLoadingMore) {
            viewModelScope.launch {
                try {
                    isLoadingMore = true
                    val nextPage = currentPage + 1
                    
                    repository.getPayments(page = nextPage)?.let { newPayments ->
                        if (newPayments.isNotEmpty()) {
                            _payments.value = _payments.value + newPayments
                            currentPage = nextPage
                        } else {
                            isLastPage = true
                        }
                    }
                } catch (e: Exception) {
                    _error.value = e.message ?: "Error al cargar más pagos"
                    Log.e("PaymentViewModel", "Error loading more payments", e)
                } finally {
                    isLoadingMore = false
                }
            }
        }
    }

    fun resetPagination() {
        currentPage = 1
        isLastPage = false
        isLoadingMore = false
        _payments.value = emptyList()
        loadPayments(1)
    }

    suspend fun makePayment(amount: Double, receiverEmail: String): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            _success.value = null
            
            repository.makePayment(amount, receiverEmail).fold(
                onSuccess = { 
                    _success.value = true
                    resetPagination() // Recargar los pagos después de un pago exitoso
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
} 