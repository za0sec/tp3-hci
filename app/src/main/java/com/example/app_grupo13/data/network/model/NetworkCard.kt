package com.example.app_grupo13.data.network.model

import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.model.CardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class NetworkCard(
    val id: Int? = null,
    val number: String,
    @SerialName("expirationDate") val expirationDate: String,
    @SerialName("fullName") val fullName: String,
    val cvv: String? = null,
    val type: String,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
) {
    fun asModel(): Card {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        return Card(
            id = id,
            number = number,
            expirationDate = expirationDate,
            fullName = fullName,
            cvv = cvv,
            type = when (type) {
                "DEBIT" -> CardType.DEBIT
                "CREDIT" -> CardType.CREDIT
                else -> CardType.CREDIT
            },
            createdAt = createdAt?.let { dateFormat.parse(it) },
            updatedAt = updatedAt?.let { dateFormat.parse(it) }
        )
    }
}
