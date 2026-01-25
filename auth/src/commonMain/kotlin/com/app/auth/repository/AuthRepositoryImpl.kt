package com.app.auth.repository

import com.app.auth.dataSource.AuthDataSource
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun loginWithEmailAndPassword(email: String, password: String) =
        authDataSource.loginWithEmailAndPassword(email = email, password = password)

    override suspend fun signUpWithEmailAndPassword(email: String, password: String) =
        authDataSource.signUpWithEmailAndPassword(email = email, password = password)

    override suspend fun signInWithGoogle(idToken: String) =
        authDataSource.signInWithGoogle(idToken = idToken)

    override suspend fun isLoggedIn() = authDataSource.isLoggedIn()
    override suspend fun logout(): Flow<Boolean> = authDataSource.logout()
    override suspend fun deleteAccount(): Flow<Boolean> = authDataSource.deleteAccount()
    override suspend fun sendPasswordResetEmail(email: String): Flow<Boolean> =
        authDataSource.sendPasswordResetEmail(email)
}