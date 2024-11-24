package com.example.app_grupo13.data.repository

import android.util.Log
import com.example.app_grupo13.data.model.VerifyData
import com.example.app_grupo13.data.network.RemoteDataSource

class VerifyRepository(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun verifyUser(code: String): Boolean {
        return try {
            Log.d("VerifyRepository", "Attempting to verify user with code")
            val response = remoteDataSource.verifyUser(VerifyData(code))
            Log.d("VerifyRepository", "Response received")
            
            true // Assuming success if no exception is thrown
        } catch (e: Exception) {
            Log.e("VerifyRepository", "Exception during verification", e)
            false
        }
    }
} 