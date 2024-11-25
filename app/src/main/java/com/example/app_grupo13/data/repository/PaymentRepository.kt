package com.example.app_grupo13.data.repository

import android.util.Log
import com.example.app_grupo13.data.model.ErrorResponse
import com.example.app_grupo13.data.model.Payment
import com.example.app_grupo13.data.network.RemoteDataSource
import kotlinx.serialization.json.Json

class PaymentRepository(private val remoteDataSource: RemoteDataSource) {

    suspend fun makePayment(amount: Double, receiverEmail: String): Result<Unit> {
        return try {
            val response = remoteDataSource.makePayment(amount, receiverEmail)
            Log.d("PaymentRepository", "Payment response code: ${response.code()}")
            
            when (response.code()) {
                200, 201 -> {
                    Log.d("PaymentRepository", "Payment successful")
                    Result.success(Unit)
                }
                400 -> {
                    val errorBody = response.errorBody()?.string()
                    Log.d("PaymentRepository", "Error body: $errorBody")
                    Result.failure(Exception("Saldo insuficiente"))
                }
                404 -> {
                    Result.failure(Exception("Usuario no encontrado"))
                }
                else -> {
                    Log.e("PaymentRepository", "Error making payment: ${response.code()}")
                    Result.failure(Exception("Error al realizar la transferencia"))
                }
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Exception making payment", e)
            Result.failure(e)
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
    ): List<Payment>? {
        return try {
            val response = remoteDataSource.getPayments(page, direction, pending, type, range, source, cardId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("PaymentRepository", "Error getting payments: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Exception getting payments", e)
            null
        }
    }

    suspend fun getPayment(paymentId: Int): Payment? {
        return try {
            val response = remoteDataSource.getPayment(paymentId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("PaymentRepository", "Error getting payment: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PaymentRepository", "Exception getting payment", e)
            null
        }
    }
} 