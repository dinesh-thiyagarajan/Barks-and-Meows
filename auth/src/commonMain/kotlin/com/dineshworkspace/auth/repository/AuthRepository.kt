package com.dineshworkspace.auth.repository

import com.dineshworkspace.auth.dataModels.AuthResponse

interface AuthRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthResponse
}