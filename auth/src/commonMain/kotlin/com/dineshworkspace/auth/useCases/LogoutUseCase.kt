package com.dineshworkspace.auth.useCases

import com.dineshworkspace.auth.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() =
        authRepository.logout()
}