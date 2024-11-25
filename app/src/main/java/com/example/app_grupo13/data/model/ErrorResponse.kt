package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String
) 