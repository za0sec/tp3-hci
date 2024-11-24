package com.example.app_grupo13.data.network.model

import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.model.CardType
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Serializable
data class  NetworkCard(
    var id: Int? = null,
    var number: String,
    var expirationDate: String,
    var fullName: String,
    var cvv: String? = null,
    var type: String,
    var createdAt: String? = null,
    var updatedAt: String? = null
) {
    fun asModel(): Card {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return Card(
            id = id,
            number = number,
            expirationDate = expirationDate,
            fullName = fullName,
            cvv = cvv,
            type = if (type == "DEBIT") CardType.DEBIT else CardType.CREDIT,
            createdAt = createdAt?.let { dateFormat.parse(it) },
            updatedAt = updatedAt?.let { dateFormat.parse(it) }
        )
    }
}
