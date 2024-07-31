package com.dineshworkspace.auth.useCases

import com.dineshworkspace.auth.repository.AuthRepository


class IsLoggedInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() =
        authRepository.isLoggedIn()
}