package com.dineshworkspace.auth.dataSource

import com.dineshworkspace.auth.dataModels.AuthResponse
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow

class AuthDataSource(private val firebaseAuth: FirebaseAuth) {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthResponse {
        try {
            val authResult =
                firebaseAuth.signInWithEmailAndPassword(email = email, password = password)
            return AuthResponse(userEmail = authResult.user?.email)
        } catch (ex: Exception) {
            return AuthResponse(message = ex.message)
        }
    }

    suspend fun isLoggedIn() = flow {
        emit(firebaseAuth.currentUser != null)
    }


}