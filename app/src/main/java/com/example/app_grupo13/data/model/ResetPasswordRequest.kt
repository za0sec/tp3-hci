package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val code: String,
    val password: String
) 