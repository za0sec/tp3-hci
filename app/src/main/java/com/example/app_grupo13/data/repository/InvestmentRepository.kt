package com.example.app_grupo13.data.repository

import android.util.Log
import com.example.app_grupo13.data.model.DailyInterest
import com.example.app_grupo13.data.model.DailyReturn
import com.example.app_grupo13.data.model.Investment
import com.example.app_grupo13.data.model.WalletDetails
import com.example.app_grupo13.data.network.RemoteDataSource

class InvestmentRepository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getCurrentInvestment(): Investment? {
        return try {
            val response = remoteDataSource.getInvestment()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("InvestmentRepository", "Error getting investment: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("InvestmentRepository", "Error getting investment", e)
            null
        }
    }

    suspend fun invest(amount: Double): WalletDetails? {
        return try {
            val response = remoteDataSource.invest(amount)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("InvestmentRepository", "Error investing: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("InvestmentRepository", "Error investing", e)
            null
        }
    }

    suspend fun divest(amount: Double): WalletDetails? {
        return try {
            val response = remoteDataSource.divest(amount)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("InvestmentRepository", "Error divesting: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("InvestmentRepository", "Error divesting", e)
            null
        }
    }

    suspend fun getDailyReturns(page: Int = 1): List<DailyReturn>? {
        return try {
            val response = remoteDataSource.getDailyReturns(page)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("InvestmentRepository", "Error getting daily returns: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("InvestmentRepository", "Error getting daily returns", e)
            null
        }
    }

    suspend fun getDailyInterest(): DailyInterest? {
        return try {
            val response = remoteDataSource.getDailyInterest()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("InvestmentRepository", "Error getting daily interest: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("InvestmentRepository", "Error getting daily interest", e)
            null
        }
    }
} 