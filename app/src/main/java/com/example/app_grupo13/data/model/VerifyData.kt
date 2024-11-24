package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyData(
    val code: String
)

@Serializable
data class VerifyResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthdate: String,
    val email: String
) 