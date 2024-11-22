package com.example.app_grupo13.data.repository

import com.example.app_grupo13.data.model.Card
import com.example.app_grupo13.data.network.RemoteDataSource

class CardRepository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getCards(): List<Card>? {
        val response = remoteDataSource.fetchCards()
        return if (response.isSuccessful) {
            response.body()?.map { it.asModel() }
        } else {
            null
        }
    }

    suspend fun addCard(card: Card): Card? {
        val networkCard = card.asNetworkModel()
        val response = remoteDataSource.addCard(networkCard)
        return if (response.isSuccessful) {
            response.body()?.asModel()
        } else {
            null
        }
    }

    suspend fun deleteCard(cardId: Int): Boolean {
        val response = remoteDataSource.deleteCard(cardId)
        return response.isSuccessful
    }
}
