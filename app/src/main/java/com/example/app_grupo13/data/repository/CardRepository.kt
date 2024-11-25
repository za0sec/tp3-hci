package com.example.app_grupo13.data.repository

import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.network.RemoteDataSource
import android.util.Log

class CardRepository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getCards(): List<Card>? {
        val response = remoteDataSource.fetchCards()
        return if (response.isSuccessful) {
            response.body()?.map { networkCard -> networkCard.asModel() }
        } else {
            Log.e("CardRepository", "Error getting cards: ${response.code()}")
            null
        }
    }

    suspend fun addCard(card: Card): Card? {
        val networkCard = card.asNetworkModel()
        val response = remoteDataSource.addCard(networkCard)
        return if (response.isSuccessful) {
            response.body()?.asModel()
        } else {
            Log.e("CardRepository", "Error adding card: ${response.code()}")
            null
        }
    }

    suspend fun deleteCard(cardId: Int): Boolean {
        val response = remoteDataSource.deleteCard(cardId)
        if (!response.isSuccessful) {
            Log.e("CardRepository", "Error deleting card: ${response.code()}")
        }
        return response.isSuccessful
    }
}
