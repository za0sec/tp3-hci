package com.example.app_grupo13.data.network

import android.content.Context
import android.util.Log
import com.example.app_grupo13.data.model.Balance
import com.example.app_grupo13.data.model.LoginData
import com.example.app_grupo13.data.model.LoginResponse
import com.example.app_grupo13.data.model.RegistrationData
import com.example.app_grupo13.data.model.User
import com.example.app_grupo13.data.model.VerifyData
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

    suspend fun fetchCards() = walletApiService.getCards()

    suspend fun addCard(networkCard: NetworkCard) = walletApiService.addCard(networkCard)

    suspend fun deleteCard(cardId: Int) = walletApiService.deleteCard(cardId)

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
}
