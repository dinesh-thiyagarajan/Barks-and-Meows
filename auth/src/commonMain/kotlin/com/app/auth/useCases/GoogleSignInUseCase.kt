package com.app.auth.useCases

import com.app.auth.repository.AuthRepository

class GoogleSignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String) =
        authRepository.signInWithGoogle(idToken = idToken)
}
