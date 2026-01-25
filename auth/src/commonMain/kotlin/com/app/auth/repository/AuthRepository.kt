package com.app.auth.repository

import com.app.auth.dataModels.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthResponse
    suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResponse
    suspend fun signInWithGoogle(idToken: String): AuthResponse
    suspend fun isLoggedIn(): Flow<Boolean>
    suspend fun logout(): Flow<Boolean>
    suspend fun deleteAccount(): Flow<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Flow<Boolean>
}