package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class DeleteAccountUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.deleteAccount()
}
