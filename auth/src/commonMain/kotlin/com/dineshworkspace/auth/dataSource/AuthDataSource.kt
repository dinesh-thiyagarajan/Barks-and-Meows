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

    suspend fun logout() = flow {
        firebaseAuth.signOut()
        emit(true)
    }

    suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResponse {
        try {
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(email = email, password = password)
            return AuthResponse(userEmail = authResult.user?.email)
        } catch (ex: Exception) {
            return AuthResponse(message = ex.message)
        }
    }

    suspend fun signInWithGoogle(idToken: String): AuthResponse {
        try {
            val credential = dev.gitlive.firebase.auth.GoogleAuthProvider.credential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential)
            return AuthResponse(userEmail = authResult.user?.email)
        } catch (ex: Exception) {
            return AuthResponse(message = ex.message)
        }
    }

}