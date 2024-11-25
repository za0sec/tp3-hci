package com.example.app_grupo13.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.utils.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentLanguage = MutableStateFlow(LocaleHelper.getLanguage(application))
    val currentLanguage: StateFlow<String> = _currentLanguage

    fun updateLanguage(context: Context, language: String) {
        viewModelScope.launch {
            LocaleHelper.setLocale(context, language)
            _currentLanguage.value = language
        }
    }
} 