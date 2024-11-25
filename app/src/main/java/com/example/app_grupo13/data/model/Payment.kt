package com.example.app_grupo13.data.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val id: Int? = null,
    val type: String? = null,
    val amount: Double? = null,
    val balanceBefore: Double? = null,
    val balanceAfter: Double? = null,
    val pending: Boolean? = null,
    val linkUuid: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,
    @Contextual val card: Card? = null
) 