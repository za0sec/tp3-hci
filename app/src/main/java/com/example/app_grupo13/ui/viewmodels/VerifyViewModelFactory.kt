package com.example.app_grupo13.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.VerifyRepository

class VerifyViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerifyViewModel::class.java)) {
            val remoteDataSource = RemoteDataSource(context)
            val verifyRepository = VerifyRepository(remoteDataSource)
            @Suppress("UNCHECKED_CAST")
            return VerifyViewModel(verifyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 