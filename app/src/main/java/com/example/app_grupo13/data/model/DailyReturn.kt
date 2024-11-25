package com.example.app_grupo13.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyReturn(
    val id: Int,
    val returnGiven: Double,
    val balanceBefore: Double,
    val balanceAfter: Double,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String
) 