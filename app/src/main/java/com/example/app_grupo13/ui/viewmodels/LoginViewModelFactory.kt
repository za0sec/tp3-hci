package com.example.app_grupo13.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_grupo13.data.local.SessionManager
import com.example.app_grupo13.data.network.RemoteDataSource
import com.example.app_grupo13.data.repository.LoginRepository

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val remoteDataSource = RemoteDataSource(context)
            val sessionManager = SessionManager(context)
            val loginRepository = LoginRepository(remoteDataSource, sessionManager)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 