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
import com.example.app_grupo13.data.model.RechargeRequest
import com.example.app_grupo13.data.model.Investment
import com.example.app_grupo13.data.model.InvestmentRequest
import com.example.app_grupo13.data.model.DailyReturn
import com.example.app_grupo13.data.model.DailyInterest
import com.example.app_grupo13.data.model.RecoverPasswordRequest
import com.example.app_grupo13.data.model.ResetPasswordRequest
import com.example.app_grupo13.data.model.Payment
import com.example.app_grupo13.data.model.PaymentRequest
import com.example.app_grupo13.data.model.PaymentResponse
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

    @POST("wallet/recharge")
    @Headers("Content-Type: application/json")
    suspend fun rechargeWallet(@Body request: RechargeRequest): Response<WalletDetails>

    @GET("wallet/investment")
    suspend fun getInvestment(): Response<Investment>

    @POST("wallet/invest")
    suspend fun invest(@Body request: InvestmentRequest): Response<WalletDetails>

    @POST("wallet/divest")
    suspend fun divest(@Body request: InvestmentRequest): Response<WalletDetails>

    @GET("wallet/daily-returns")
    suspend fun getDailyReturns(@Query("page") page: Int = 1): Response<List<DailyReturn>>

    @GET("wallet/daily-interest")
    suspend fun getDailyInterest(): Response<DailyInterest>

    @POST("user/recover-password")
    @Headers("Content-Type: application/json")
    suspend fun recoverPassword(@Body request: RecoverPasswordRequest): Response<Unit>

    @POST("user/reset-password")
    @Headers("Content-Type: application/json")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Unit>

    @POST("payment")
    suspend fun makePayment(@Body request: PaymentRequest): Response<Payment>

    @GET("payment")
    suspend fun getPayments(
        @Query("page") page: Int = 1,
        @Query("direction") direction: String = "ASC",
        @Query("pending") pending: Boolean? = null,
        @Query("type") type: String? = null,
        @Query("range") range: String? = null,
        @Query("source") source: String? = null,
        @Query("cardId") cardId: Int? = null
    ): Response<PaymentResponse>

    @GET("payment/{paymentId}")
    suspend fun getPayment(@Path("paymentId") paymentId: Int): Response<Payment>
}
