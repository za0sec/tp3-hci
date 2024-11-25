package com.example.app_grupo13.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_grupo13.data.network.RemoteDataSource

class DepositViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
            val remoteDataSource = RemoteDataSource(context)
            @Suppress("UNCHECKED_CAST")
            return DepositViewModel(remoteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 