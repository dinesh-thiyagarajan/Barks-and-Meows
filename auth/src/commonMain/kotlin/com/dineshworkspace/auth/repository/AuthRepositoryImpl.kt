package com.dineshworkspace.auth.repository

import com.dineshworkspace.auth.dataSource.AuthDataSource

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun loginWithEmailAndPassword(email: String, password: String) =
        authDataSource.loginWithEmailAndPassword(email = email, password = password)
}