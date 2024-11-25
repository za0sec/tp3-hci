package com.example.app_grupo13.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.InvestmentRepository

class InvestmentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvestmentViewModel::class.java)) {
            val remoteDataSource = RemoteDataSource(context)
            val repository = InvestmentRepository(remoteDataSource)
            @Suppress("UNCHECKED_CAST")
            return InvestmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 