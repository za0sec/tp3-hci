package com.example.app_grupo13.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDetails(
    val id: Int? = null,
    val balance: Double? = null,
    val invested: Double? = null,
    val cbu: String? = null,
    val alias: String? = null,
) 