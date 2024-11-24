package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String
) 