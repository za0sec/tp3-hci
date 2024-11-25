package com.example.app_grupo13.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        PreferencesManager.setLanguage(context, language)
        
        return context.createConfigurationContext(config)
    }

    fun getLanguage(context: Context): String {
        return PreferencesManager.getLanguage(context)
    }
} 