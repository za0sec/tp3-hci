package com.example.app_grupo13.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo13.data.model.RegistrationData
import com.example.app_grupo13.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.Calendar
class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val registrationResult = mutableStateOf<Boolean?>(null)

    fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        val registrationData = RegistrationData(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            birthDate = "2002-01-01"
        )

        viewModelScope.launch {
            try {
                val result = userRepository.registerUser(registrationData)
                registrationResult.value = result
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error en registro", e)
                registrationResult.value = false
            }
        }
    }
}
