package com.example.app_grupo13.data.model

import com.example.app_grupo13.data.network.model.NetworkCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Card(
    var id: Int? = null,
    var number: String,
    var expirationDate: String,
    var fullName: String,
    var cvv: String? = null,
    var type: CardType,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
) {
    fun asNetworkModel(): NetworkCard {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return NetworkCard(
            id = id,
            number = number,
            expirationDate = expirationDate,
            fullName = fullName,
            cvv = cvv,
            type = when (type) {
                CardType.DEBIT -> "DEBIT"
                CardType.CREDIT -> "CREDIT"
            },
            createdAt = createdAt?.let { dateFormat.format(it) },
            updatedAt = updatedAt?.let { dateFormat.format(it) }
        )
    }
}
