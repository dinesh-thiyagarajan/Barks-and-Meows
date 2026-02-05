package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class IsLoggedInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() =
        authRepository.isLoggedIn()
}
