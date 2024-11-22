package com.example.app_grupo13.data.network

import com.example.app_grupo13.data.network.api.WalletApiService
import com.example.app_grupo13.data.network.model.NetworkCard
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource( walletApiService: WalletApiService) {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val walletApiService: WalletApiService = retrofit.create(WalletApiService::class.java)


    suspend fun fetchCards() = walletApiService.getCards()

    suspend fun addCard(networkCard: NetworkCard) = walletApiService.addCard(networkCard)

    suspend fun deleteCard(cardId: Int) = walletApiService.deleteCard(cardId)
}