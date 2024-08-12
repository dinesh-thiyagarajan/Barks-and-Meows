package com.dineshworkspace.auth.repository

import com.dineshworkspace.auth.dataSource.AuthDataSource
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun loginWithEmailAndPassword(email: String, password: String) =
        authDataSource.loginWithEmailAndPassword(email = email, password = password)

    override suspend fun isLoggedIn() = authDataSource.isLoggedIn()
    override suspend fun logout(): Flow<Boolean> = authDataSource.logout()
}