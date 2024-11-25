package com.example.app_grupo13.data.network

import android.content.Context
import android.util.Log
import com.example.app_grupo13.data.model.Balance
import com.example.app_grupo13.data.model.DailyInterest
import com.example.app_grupo13.data.model.DailyReturn
import com.example.app_grupo13.data.model.Investment
import com.example.app_grupo13.data.model.InvestmentRequest
import com.example.app_grupo13.data.model.LoginData
import com.example.app_grupo13.data.model.LoginResponse
import com.example.app_grupo13.data.model.Payment
import com.example.app_grupo13.data.model.PaymentRequest
import com.example.app_grupo13.data.model.RechargeRequest
import com.example.app_grupo13.data.model.RecoverPasswordRequest
import com.example.app_grupo13.data.model.RegistrationData
import com.example.app_grupo13.data.model.ResetPasswordRequest
import com.example.app_grupo13.data.model.UpdateAliasRequest
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.model.VerifyData
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.network.api.WalletApiService
import com.example.app_grupo13.data.network.model.NetworkCard
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit


class RemoteDataSource(private val context: Context) {

    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/api/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build())
        .build()

    private val walletApiService: WalletApiService = retrofit.create(WalletApiService::class.java)

    private var _lastResponseWasSuccessful = false
    val lastResponseWasSuccessful: Boolean get() = _lastResponseWasSuccessful

    suspend fun fetchCards(): Response<List<NetworkCard>> {
        return try {
            Log.d("RemoteDataSource", "Fetching cards...")
            val response = walletApiService.getCards()
            Log.d("RemoteDataSource", "Cards response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching cards", e)
            throw e
        }
    }

    suspend fun addCard(card: NetworkCard): Response<NetworkCard> {
        return try {
            Log.d("RemoteDataSource", "Adding card: $card")
            val response = walletApiService.addCard(card)
            Log.d("RemoteDataSource", "Add card response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error adding card", e)
            throw e
        }
    }

    suspend fun deleteCard(cardId: Int): Response<Unit> {
        return try {
            Log.d("RemoteDataSource", "Deleting card with ID: $cardId")
            val response = walletApiService.deleteCard(cardId)
            Log.d("RemoteDataSource", "Delete card response code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error deleting card", e)
            throw e
        }
    }

    suspend fun registerUser(registrationData: RegistrationData) {
        val jsonString = json.encodeToString(registrationData)
        Log.d("RemoteDataSource", "Sending JSON: $jsonString")
        
        val response = walletApiService.registerUser(registrationData)
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string()
            Log.e("RemoteDataSource", "Error: ${response.code()} - $errorBody")
            throw Exception("Error en el registro: ${response.code()} - $errorBody")
        }
    }

    suspend fun loginUser(loginData: LoginData): Response<LoginResponse> {
        Log.d("RemoteDataSource", "Making login API call with data: $loginData")
        try {
            val jsonString = json.encodeToString(loginData)
            Log.d("RemoteDataSource", "Sending JSON: $jsonString")
            
            val response = walletApiService.loginUser(loginData)
            Log.d("RemoteDataSource", "Login API response code: ${response.code()}")
            Log.d("RemoteDataSource", "Login API response body: ${response.body()}")
            Log.d("RemoteDataSource", "Login API error body: ${response.errorBody()?.string()}")
            return response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Exception during API call", e)
            throw e
        }
    }

    suspend fun verifyUser(verifyData: VerifyData): Boolean {
        val response = walletApiService.verifyUser(verifyData)
        return response.isSuccessful
    }

    suspend fun getUser(): Response<User> {
        return walletApiService.getUser()
    }

    suspend fun getBalance(): Response<Balance> {
        return walletApiService.getBalance()
    }

    suspend fun getWalletDetails(): Response<WalletDetails> {
        return try {
            Log.d("RemoteDataSource", "Fetching wallet details...")
            val response = walletApiService.getWalletDetails()
            Log.d("RemoteDataSource", "Wallet details response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching wallet details", e)
            throw e
        }
    }

    suspend fun updateAlias(newAlias: String): Response<WalletDetails> {
        return try {
            Log.d("RemoteDataSource", "Updating alias to: $newAlias")
            val request = UpdateAliasRequest(alias = newAlias)
            val response = walletApiService.updateAlias(request)
            Log.d("RemoteDataSource", "Update alias response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error updating alias", e)
            throw e
        }
    }

    suspend fun rechargeWallet(amount: Double): Response<WalletDetails> {
        return try {
            Log.d("RemoteDataSource", "Recharging wallet with amount: $amount")
            val request = RechargeRequest(amount)
            val response = walletApiService.rechargeWallet(request)
            Log.d("RemoteDataSource", "Recharge response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error recharging wallet", e)
            throw e
        }
    }

    suspend fun getInvestment(): Response<Investment> {
        return try {
            Log.d("RemoteDataSource", "Fetching investment...")
            val response = walletApiService.getInvestment()
            Log.d("RemoteDataSource", "Investment response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching investment", e)
            throw e
        }
    }

    suspend fun invest(amount: Double): Response<WalletDetails> {
        return try {
            Log.d("RemoteDataSource", "Investing amount: $amount")
            val request = InvestmentRequest(amount)
            val response = walletApiService.invest(request)
            Log.d("RemoteDataSource", "Invest response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error investing", e)
            throw e
        }
    }

    suspend fun divest(amount: Double): Response<WalletDetails> {
        return try {
            Log.d("RemoteDataSource", "Divesting amount: $amount")
            val request = InvestmentRequest(amount)
            val response = walletApiService.divest(request)
            Log.d("RemoteDataSource", "Divest response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error divesting", e)
            throw e
        }
    }

    suspend fun getDailyReturns(page: Int = 1): Response<List<DailyReturn>> {
        return try {
            Log.d("RemoteDataSource", "Fetching daily returns for page: $page")
            val response = walletApiService.getDailyReturns(page)
            Log.d("RemoteDataSource", "Daily returns response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching daily returns", e)
            throw e
        }
    }

    suspend fun getDailyInterest(): Response<DailyInterest> {
        return try {
            Log.d("RemoteDataSource", "Fetching daily interest...")
            val response = walletApiService.getDailyInterest()
            Log.d("RemoteDataSource", "Daily interest response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching daily interest", e)
            throw e
        }
    }

    suspend fun recoverPassword(email: String): Response<Unit> {
        return try {
            Log.d("RemoteDataSource", "Requesting password recovery for email: $email")
            val request = RecoverPasswordRequest(email)
            val response = walletApiService.recoverPassword(request)
            Log.d("RemoteDataSource", "Password recovery response code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error requesting password recovery", e)
            throw e
        }
    }

    suspend fun resetPassword(code: String, newPassword: String): Response<Unit> {
        return try {
            Log.d("RemoteDataSource", "Resetting password with code: $code")
            val request = ResetPasswordRequest(code, newPassword)
            val response = walletApiService.resetPassword(request)
            Log.d("RemoteDataSource", "Password reset response code: ${response.code()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error resetting password", e)
            throw e
        }
    }

    suspend fun makePayment(amount: Double, receiverEmail: String): Response<Payment> {
        return try {
            Log.d("RemoteDataSource", "Making payment: amount=$amount, to=$receiverEmail")
            val request = PaymentRequest(
                amount = amount,
                description = "Transferencia",
                type = "BALANCE",
                receiverEmail = receiverEmail
            )
            val response = walletApiService.makePayment(request)
            _lastResponseWasSuccessful = response.isSuccessful
            Log.d("RemoteDataSource", "Payment response code: ${response.code()}")
            Log.d("RemoteDataSource", "Payment response body: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error making payment", e)
            throw e
        }
    }

    suspend fun getPayments(
        page: Int = 1,
        direction: String = "ASC",
        pending: Boolean? = null,
        type: String? = null,
        range: String? = null,
        source: String? = null,
        cardId: Int? = null
    ): Response<List<Payment>> {
        return try {
            Log.d("RemoteDataSource", "Fetching payments with filters: page=$page, direction=$direction")
            val response = walletApiService.getPayments(page, direction, pending, type, range, source, cardId)
            Log.d("RemoteDataSource", "Payments response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching payments", e)
            throw e
        }
    }

    suspend fun getPayment(paymentId: Int): Response<Payment> {
        return try {
            Log.d("RemoteDataSource", "Fetching payment: $paymentId")
            val response = walletApiService.getPayment(paymentId)
            Log.d("RemoteDataSource", "Payment response: ${response.body()}")
            response
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching payment", e)
            throw e
        }
    }
}
