package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val onLogout: (() -> Unit)? = null
) {
    suspend operator fun invoke() = authRepository.logout().also {
        onLogout?.invoke()
    }
}
