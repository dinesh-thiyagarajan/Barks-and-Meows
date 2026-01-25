package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class ForgotPasswordUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String) =
        authRepository.sendPasswordResetEmail(email)
}
