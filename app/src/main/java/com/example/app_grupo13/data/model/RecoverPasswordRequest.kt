package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecoverPasswordRequest(
    val email: String
) 