package com.example.app_grupo13.data.network.api

import com.example.app_grupo13.data.model.LoginData
import com.example.app_grupo13.data.model.LoginResponse
import com.example.app_grupo13.data.model.RegistrationData
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.network.model.NetworkCard
import com.example.app_grupo13.data.model.VerifyData
import com.example.app_grupo13.data.model.VerifyResponse
import com.example.app_grupo13.data.model.Balance
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.model.UpdateAliasRequest
import retrofit2.Response
import retrofit2.http.*

interface WalletApiService {

    @POST("user")
    @Headers("Content-Type: application/json")
    suspend fun registerUser(@Body registrationData: RegistrationData): Response<Unit>

    @POST("user/login")
    @Headers("Content-Type: application/json")
    suspend fun loginUser(@Body loginData: LoginData): Response<LoginResponse>

    @POST("user/verify")
    @Headers("Content-Type: application/json")
    suspend fun verifyUser(@Body verifyData: VerifyData): Response<VerifyResponse>

    @GET("user")
    @Headers("Content-Type: application/json")
    suspend fun getUser(): Response<User>

    @GET("wallet/balance")
    @Headers("Content-Type: application/json")
    suspend fun getBalance(): Response<Balance>

    @GET("wallet/cards")
    suspend fun getCards(): Response<List<NetworkCard>>

    @POST("wallet/cards")
    suspend fun addCard(@Body card: NetworkCard): Response<NetworkCard>

    @DELETE("wallet/cards/{cardId}")
    suspend fun deleteCard(@Path("cardId") cardId: Int): Response<Unit>

    @GET("wallet/details")
    @Headers("Content-Type: application/json")
    suspend fun getWalletDetails(): Response<WalletDetails>

    @PUT("wallet/update-alias")
    @Headers("Content-Type: application/json")
    suspend fun updateAlias(@Body request: UpdateAliasRequest): Response<WalletDetails>
}
