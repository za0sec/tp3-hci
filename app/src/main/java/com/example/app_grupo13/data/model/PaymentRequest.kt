package com.example.app_grupo13.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val amount: Double,
    val description: String = "Transferencia",
    val type: String = "BALANCE",
    val receiverEmail: String
) 