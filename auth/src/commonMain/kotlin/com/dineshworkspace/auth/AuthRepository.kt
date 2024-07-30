package com.dineshworkspace.auth

interface AuthRepository {
    suspend fun login()
}