package com.example.app_grupo13.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDetails(
    val id: Int,
    val balance: Double,
    val invested: Double,
    val cbu: String,
    val alias: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String
) 