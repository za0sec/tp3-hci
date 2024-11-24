package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.repository.VerifyRepository
import kotlinx.coroutines.launch

class VerifyViewModel(private val verifyRepository: VerifyRepository) : ViewModel() {
    val verifyResult = mutableStateOf<Boolean?>(null)
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun verifyUser(code: String) {
        if (code.isBlank()) {
            Log.e("VerifyViewModel", "Code is blank")
            verifyResult.value = false
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = verifyRepository.verifyUser(code)
                verifyResult.value = result
            } catch (e: Exception) {
                Log.e("VerifyViewModel", "Error en verificación", e)
                verifyResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
} 