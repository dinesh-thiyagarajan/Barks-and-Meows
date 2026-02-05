package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.loginWithEmailAndPassword(email = email, password = password)
}
