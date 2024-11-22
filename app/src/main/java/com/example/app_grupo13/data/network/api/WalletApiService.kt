package com.example.app_grupo13.data.network.api

import com.example.app_grupo13.data.network.model.NetworkCard
import retrofit2.Response
import retrofit2.http.*

interface WalletApiService {

    @GET("wallet/cards")
    suspend fun getCards(): Response<List<NetworkCard>>

    @POST("wallet/cards")
    suspend fun addCard(@Body card: NetworkCard): Response<NetworkCard>

    @DELETE("wallet/cards/{cardId}")
    suspend fun deleteCard(@Path("cardId") cardId: Int): Response<Unit>
}
