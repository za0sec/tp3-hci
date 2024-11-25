package com.example.app_grupo13.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.PaymentRepository

class PaymentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            val remoteDataSource = RemoteDataSource(context)
            val repository = PaymentRepository(remoteDataSource)
            @Suppress("UNCHECKED_CAST")
            return PaymentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 