package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val birthDate: String
)