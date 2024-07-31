package com.dineshworkspace.auth.repository

import com.dineshworkspace.auth.dataModels.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthResponse
    suspend fun isLoggedIn(): Flow<Boolean>
}