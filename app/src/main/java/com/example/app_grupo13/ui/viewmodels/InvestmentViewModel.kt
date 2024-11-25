package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.DailyInterest
import com.example.app_grupo13.data.model.DailyReturn
import com.example.app_grupo13.data.model.Investment
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.repository.InvestmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InvestmentViewModel(
    private val repository: InvestmentRepository
) : ViewModel() {
    
    private val _currentInvestment = MutableStateFlow<Investment?>(null)
    val currentInvestment: StateFlow<Investment?> = _currentInvestment
    
    private val _dailyReturns = MutableStateFlow<List<DailyReturn>>(emptyList())
    val dailyReturns: StateFlow<List<DailyReturn>> = _dailyReturns
    
    private val _dailyInterest = MutableStateFlow<DailyInterest?>(null)
    val dailyInterest: StateFlow<DailyInterest?> = _dailyInterest
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadInvestmentData()
    }

    private fun loadInvestmentData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Cargar inversión actual
                _currentInvestment.value = repository.getCurrentInvestment()
                
                // Cargar retornos diarios
                repository.getDailyReturns()?.let { returns ->
                    _dailyReturns.value = returns
                }
                
                // Cargar tasa de interés diaria
                _dailyInterest.value = repository.getDailyInterest()
                
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
                Log.e("InvestmentViewModel", "Error loading investment data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun invest(amount: Double): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            
            repository.invest(amount)?.let {
                loadInvestmentData() // Recargar datos después de invertir
                true
            } ?: false
        } catch (e: Exception) {
            _error.value = e.message ?: "Error al realizar la inversión"
            Log.e("InvestmentViewModel", "Error investing", e)
            false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun divest(amount: Double): Boolean {
        return try {
            _isLoading.value = true
            _error.value = null
            
            repository.divest(amount)?.let {
                loadInvestmentData() // Recargar datos después de desinvertir
                true
            } ?: false
        } catch (e: Exception) {
            _error.value = e.message ?: "Error al retirar la inversión"
            Log.e("InvestmentViewModel", "Error divesting", e)
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun reloadData() {
        loadInvestmentData()
    }
} 